/*
 *
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.selcukes.extent.report;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.Asterisk;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Test;
import io.cucumber.messages.types.Examples;
import io.cucumber.messages.types.Feature;
import io.cucumber.messages.types.Scenario;
import io.cucumber.messages.types.Step;
import io.cucumber.messages.types.TableCell;
import io.cucumber.messages.types.TableRow;
import io.cucumber.messages.types.Tag;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.DataTableArgument;
import io.cucumber.plugin.event.DocStringArgument;
import io.cucumber.plugin.event.EmbedEvent;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.HookTestStep;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.StepArgument;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import io.cucumber.plugin.event.WriteEvent;
import io.github.selcukes.commons.helper.CollectionUtils;
import io.github.selcukes.commons.helper.ExceptionHelper;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static io.github.selcukes.extent.report.Reporter.getReporter;

public class SelcukesExtentAdapter implements ConcurrentEventListener {

    private static final Map<String, String> MIME_TYPES_EXTENSIONS = Map.of(
        "image/bmp", "bmp",
        "image/gif", "gif",
        "image/jpeg", "jpeg",
        "image/jpg", "jpg",
        "image/png", "png",
        "image/svg+xml", "svg");

    private static final Map<String, ExtentTest> featureMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
    private static final Map<String, ExtentTest> scenarioOutlineMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
    private static final ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
    private static final ThreadLocal<Boolean> isHookThreadLocal = new InheritableThreadLocal<>();
    private static final ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();
    private static final ThreadLocal<Set<String>> featureTagsThreadLocal = new InheritableThreadLocal<>();
    private static final ThreadLocal<Set<String>> scenarioOutlineTagsThreadLocal = new InheritableThreadLocal<>();
    private final TestSourcesModel testSources = new TestSourcesModel();

    private final ThreadLocal<URI> currentFeatureFile = new InheritableThreadLocal<>();
    private final ThreadLocal<Scenario> currentScenarioOutline = new InheritableThreadLocal<>();
    private final ThreadLocal<Examples> currentExamples = new InheritableThreadLocal<>();

    private final EventHandler<TestSourceRead> testSourceReadHandler = this::handleTestSourceRead;
    private final EventHandler<TestCaseStarted> caseStartedHandler = this::handleTestCaseStarted;
    private final EventHandler<TestStepStarted> stepStartedHandler = this::handleTestStepStarted;
    private final EventHandler<TestStepFinished> stepFinishedHandler = this::handleTestStepFinished;
    private final EventHandler<EmbedEvent> embedEventHandler = this::handleEmbed;
    private final EventHandler<WriteEvent> writeEventHandler = this::handleWrite;
    private final EventHandler<TestRunFinished> runFinishedHandler = this::handleTestRunFinished;

    @SuppressWarnings("unused")
    public SelcukesExtentAdapter(String arg) {
        ExtentService.getInstance();
    }

    public static synchronized void addTestStepLog(final String message) {
        stepTestThreadLocal.get().info(message);
    }

    public static void attachScreenshot(byte[] screenshot) {
        stepTestThreadLocal.get().info("", MediaEntityBuilder
                .createScreenCaptureFromBase64String(Base64.getEncoder().encodeToString(screenshot))
                .build());
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, testSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(EmbedEvent.class, embedEventHandler);
        publisher.registerHandlerFor(WriteEvent.class, writeEventHandler);
        publisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.getUri(), event);
    }

    private synchronized void handleTestCaseStarted(TestCaseStarted event) {
        getReporter().start();
        handleStartOfFeature(event.getTestCase());
        handleScenarioOutline(event.getTestCase());
        createTestCase(event.getTestCase());
    }

    private synchronized void handleTestStepStarted(TestStepStarted event) {
        getReporter().attachAndRestart();
        isHookThreadLocal.set(false);

        if (event.getTestStep() instanceof HookTestStep) {
            ExtentTest t = scenarioThreadLocal.get().createNode(Asterisk.class, event.getTestStep().getCodeLocation(),
                (((HookTestStep) event.getTestStep()).getHookType()).toString().toUpperCase());
            stepTestThreadLocal.set(t);
            isHookThreadLocal.set(true);
        }

        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            createTestStep(testStep);
        }
    }

    private synchronized void handleTestStepFinished(TestStepFinished event) {
        getReporter().attachAndRestart();
        updateResult(event.getResult());
        if (!event.getResult().getStatus().isOk() && event.getResult().getError() != null) {
            ExceptionHelper.handleException(event.getResult().getError());
        }

    }

    private synchronized void updateResult(Result result) {
        Test test = stepTestThreadLocal.get().getModel();
        switch (result.getStatus().name().toLowerCase()) {
            case "failed":
            case "pending":
                stepTestThreadLocal.get().fail(result.getError());
                break;
            case "undefined":
                stepTestThreadLocal.get().fail("Step undefined");
                break;
            case "skipped":
                if (isHookThreadLocal.get().equals(Boolean.TRUE)) {
                    ExtentService.getInstance().removeTest(stepTestThreadLocal.get());
                    break;
                }
                boolean currentEndingEventSkipped = test.hasLog()
                        && test.getLogs().get(test.getLogs().size() - 1).getStatus() == Status.SKIP;
                if (result.getError() != null) {
                    stepTestThreadLocal.get().skip(result.getError());
                }
                if (!currentEndingEventSkipped) {
                    String details = result.getError() == null ? "Step skipped" : result.getError().getMessage();
                    stepTestThreadLocal.get().skip(details);
                }
                break;
            case "passed":
                if (stepTestThreadLocal.get() != null) {
                    if (isHookThreadLocal.get().equals(Boolean.TRUE)) {
                        boolean mediaLogs = test.getLogs().stream().anyMatch(l -> l.getMedia() != null);
                        if (!test.hasLog() && !mediaLogs) {
                            ExtentService.getInstance().removeTest(stepTestThreadLocal.get());
                        }
                    }
                    stepTestThreadLocal.get().pass("");
                }
                break;
            default:
                break;
        }
    }

    private synchronized void handleEmbed(EmbedEvent event) {

        String mimeType = event.getMediaType();
        String extension = MIME_TYPES_EXTENSIONS.get(mimeType);
        if (extension != null) {
            if (stepTestThreadLocal.get() == null) {
                ExtentTest t = scenarioThreadLocal.get().createNode(Asterisk.class, "Embed");
                stepTestThreadLocal.set(t);
            }

            String title = event.getName() == null ? "" : event.getName();
            if (mimeType.startsWith("image/")) {
                stepTestThreadLocal.get().info(title, MediaEntityBuilder
                        .createScreenCaptureFromBase64String(Base64.getEncoder().encodeToString(event.getData()))
                        .build());
            }
        }
    }

    private void handleWrite(WriteEvent event) {
        String text = event.getText();
        if (text != null && !text.isEmpty()) {
            stepTestThreadLocal.get().info(text);
        }
    }

    private void handleTestRunFinished(TestRunFinished testRunFinished) {
        finishReport();
        getReporter().removeReporter();
        featureTestThreadLocal.remove();
        scenarioThreadLocal.remove();
        isHookThreadLocal.remove();
        stepTestThreadLocal.remove();
        featureTagsThreadLocal.remove();
        scenarioOutlineTagsThreadLocal.remove();
        currentFeatureFile.remove();
    }

    private void finishReport() {
        getReporter().attachAndClear();
        ExtentService.getInstance().flush();
    }

    private synchronized void handleStartOfFeature(TestCase testCase) {
        if (!testCase.getUri().equals(currentFeatureFile.get())) {
            Objects.requireNonNull(currentFeatureFile).set(testCase.getUri());
            createFeature(testCase);
        }
    }

    @SneakyThrows
    private synchronized void createFeature(TestCase testCase) {
        Feature feature = testSources.getFeature(testCase.getUri());

        ExtentService.getInstance().setGherkinDialect(Objects.requireNonNull(feature).getLanguage());

        if (featureMap.containsKey(feature.getName())) {
            featureTestThreadLocal.set(featureMap.get(feature.getName()));
            return;
        }
        if (featureTestThreadLocal.get() != null
                && featureTestThreadLocal.get().getModel().getName().equals(feature.getName())) {
            return;
        }
        ExtentTest t = ExtentService.getInstance().createTest(
            com.aventstack.extentreports.gherkin.model.Feature.class, feature.getName(),
            feature.getDescription());
        featureTestThreadLocal.set(t);
        featureMap.put(feature.getName(), t);

        Set<String> tagList = feature.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
        featureTagsThreadLocal.set(tagList);

    }

    private synchronized void handleScenarioOutline(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile.get(),
            testCase.getLocation().getLine());
        Scenario scenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode);

        if (Objects.requireNonNull(scenarioDefinition).getKeyword().equals("Scenario Outline")) {
            if (currentScenarioOutline.get() == null
                    || !currentScenarioOutline.get().getName().equals(scenarioDefinition.getName())) {
                scenarioOutlineThreadLocal.remove();
                createScenarioOutline(scenarioDefinition);
                currentScenarioOutline.set(scenarioDefinition);
            }
            Examples examples = (Examples) Objects.requireNonNull(astNode).parent.node;
            if (currentExamples.get() == null || !currentExamples.get().equals(examples)) {
                currentExamples.set(examples);
                createExamples(examples);
            }
        } else {
            scenarioOutlineThreadLocal.remove();
            currentScenarioOutline.remove();
            currentExamples.remove();
        }
    }

    private synchronized void createScenarioOutline(Scenario scenarioOutline) {
        if (scenarioOutlineMap.containsKey(scenarioOutline.getName())) {
            scenarioOutlineThreadLocal.set(scenarioOutlineMap.get(scenarioOutline.getName()));
            return;
        }
        if (scenarioOutlineThreadLocal.get() == null) {
            ExtentTest t = featureTestThreadLocal.get().createNode(
                com.aventstack.extentreports.gherkin.model.ScenarioOutline.class, scenarioOutline.getName(),
                scenarioOutline.getDescription());
            scenarioOutlineThreadLocal.set(t);
            scenarioOutlineMap.put(scenarioOutline.getName(), t);

            Set<String> tagList = scenarioOutline.getTags().stream().map(Tag::getName)
                    .collect(Collectors.toSet());
            scenarioOutlineTagsThreadLocal.set(tagList);
        }
    }

    private void createExamples(Examples examples) {
        List<TableRow> rows = new ArrayList<>();
        examples.getTableHeader().ifPresent(rows::add);
        rows.addAll(examples.getTableBody());
        String[][] data = getTable(rows);
        String markup = MarkupHelper.createTable(data).getMarkup();
        if (examples.getName() != null && !examples.getName().isEmpty()) {
            markup = examples.getName() + markup;
        }
        markup = scenarioOutlineThreadLocal.get().getModel().getDescription() + markup;
        scenarioOutlineThreadLocal.get().getModel().setDescription(markup);
    }

    private String[][] getTable(List<TableRow> rows) {
        return rows.stream().map(row -> row.getCells().stream()
                .map(TableCell::getValue).toArray(String[]::new))
                .toArray(String[][]::new);
    }

    private synchronized void createTestCase(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile.get(),
            testCase.getLocation().getLine());
        if (astNode != null) {
            Scenario scenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode);
            ExtentTest parent = scenarioOutlineThreadLocal.get() != null ? scenarioOutlineThreadLocal.get()
                    : featureTestThreadLocal.get();
            ExtentTest t = parent.createNode(com.aventstack.extentreports.gherkin.model.Scenario.class,
                testCase.getName(), Objects.requireNonNull(scenarioDefinition).getDescription());
            scenarioThreadLocal.set(t);
        }
        if (!testCase.getTags().isEmpty()) {
            testCase.getTags().forEach(x -> scenarioThreadLocal.get().assignCategory(x));
        }
        if (featureTagsThreadLocal.get() != null) {
            featureTagsThreadLocal.get().forEach(x -> scenarioThreadLocal.get().assignCategory(x));
        }

        Test parent = scenarioThreadLocal.get().getModel().getParent();
        if (parent.getBddType() == ScenarioOutline.class && scenarioOutlineTagsThreadLocal.get() != null) {
            scenarioOutlineTagsThreadLocal.get().forEach(x -> scenarioThreadLocal.get().assignCategory(x));
        }
    }

    @SneakyThrows
    private synchronized void createTestStep(PickleStepTestStep testStep) {
        String stepName = testStep.getStep().getText();
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile.get(),
            testStep.getStep().getLine());
        if (astNode != null) {
            Step step = (Step) astNode.node;

            String name = stepName == null || stepName.isEmpty()
                    ? step.getText().replace("<", "&lt;").replace(">", "&gt;")
                    : stepName;
            ExtentTest t = scenarioThreadLocal.get().createNode(new GherkinKeyword(step.getKeyword().trim()),
                "<b>" + step.getKeyword() + name + "</b>", testStep.getCodeLocation());
            stepTestThreadLocal.set(t);

        }
        StepArgument argument = testStep.getStep().getArgument();
        if (argument != null) {
            if (argument instanceof DocStringArgument) {
                stepTestThreadLocal.get()
                        .pass(MarkupHelper.createCodeBlock(((DocStringArgument) argument).getContent()));
            } else if (argument instanceof DataTableArgument) {
                stepTestThreadLocal.get()
                        .pass(MarkupHelper.createTable(createDataTableList((DataTableArgument) argument)));
            }
        }
    }

    private String[][] createDataTableList(DataTableArgument dataTable) {
        return CollectionUtils.toArray(dataTable.cells());
    }

}
