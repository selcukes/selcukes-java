/*
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
 */

package io.github.selcukes.reports.cucumber;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.extent.report.TestSourcesModel;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

public class CucumberLiveReportListener implements ConcurrentEventListener {
    private final Logger logger = LoggerFactory.getLogger(CucumberLiveReportListener.class);
    private final TestSourcesModel testSources = new TestSourcesModel();
    private final ThreadLocal<FeatureContext> featureContextThreadLocal = new InheritableThreadLocal<>();
    private final ThreadLocal<List<ScenarioContext>> scenarioContextThreadLocal = new InheritableThreadLocal<>();


    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::beforeTest);
        publisher.registerHandlerFor(TestSourceRead.class, this::getTestSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, this::beforeScenario);
        publisher.registerHandlerFor(TestStepStarted.class, this::beforeStep);
        publisher.registerHandlerFor(TestStepFinished.class, this::afterStep);
        publisher.registerHandlerFor(TestCaseFinished.class, this::afterScenario);
        publisher.registerHandlerFor(TestRunFinished.class, this::afterTest);
    }

    private synchronized void getTestSourceReadHandler(TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.getUri(), event);
    }

    private synchronized void beforeTest(TestRunStarted event) {
        //Do Nothing
    }


    private synchronized void beforeScenario(TestCaseStarted event) {
        String currentFeature = Objects.requireNonNull(testSources.getFeature(event.getTestCase().getUri())).getName();
        if (featureContextThreadLocal.get() == null) {
            scenarioContextThreadLocal.set(new ArrayList<>(Collections.emptyList()));
            FeatureContext featureContext = FeatureContext.builder()
                .featureName(currentFeature)
                .scenarioContexts(scenarioContextThreadLocal.get())
                .build();

            featureContextThreadLocal.set(featureContext);
        }

        if (!featureContextThreadLocal.get().getFeatureName().equalsIgnoreCase(currentFeature)) {
            featureContextThreadLocal.get().setStatus(getFeatureStatus(featureContextThreadLocal.get().getScenarioContexts()));
            logger.info(() -> String.format("Before Scenario: %n Feature[%s] %n Scenarios[%s] %n Status [%s]",
                featureContextThreadLocal.get().getFeatureName(),
                getScenarios(featureContextThreadLocal.get().getScenarioContexts()),
                featureContextThreadLocal.get().getStatus()
            ));
            LiveReportHelper.publishResults(featureContextThreadLocal.get(), "feature");
            featureContextThreadLocal.remove();
            scenarioContextThreadLocal.remove();
            scenarioContextThreadLocal.set(new ArrayList<>(Collections.emptyList()));
            FeatureContext featureContext = FeatureContext.builder()
                .featureName(currentFeature)
                .scenarioContexts(scenarioContextThreadLocal.get())
                .build();

            featureContextThreadLocal.set(featureContext);
        }
    }


    private synchronized void beforeStep(TestStepStarted event) {
      //Do Nothing

    }

    private synchronized void afterStep(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            logger.info(() -> String.format("After Step: %n Line [%s]%n Step[%s]%n Status[%s]%n Duration[%s]",
                testStep.getStep().getLine(), testStep.getStep().getText(),
                event.getResult().getStatus(), event.getResult().getDuration().toMillis() + "ms"
            ));
            StepContext stepContext = StepContext.builder()
                .stepName(testStep.getStep().getText())
                .status(event.getResult().getStatus().name())
                .duration(event.getResult().getDuration().toMillis() + "ms")
                .build();
            LiveReportHelper.publishResults(stepContext, "step");
        }

    }


    private synchronized void afterScenario(TestCaseFinished event) {
        String error = event.getResult().getStatus().is(Status.FAILED) ?
            event.getResult().getError().getMessage() : "";
        ScenarioContext scenarioContext = ScenarioContext.builder()
            .scenarioName(event.getTestCase().getName())
            .status(event.getResult().getStatus().name())
            .duration(event.getResult().getDuration().toMillis() + "ms")
            .error(error)
            .build();
        LiveReportHelper.publishResults(scenarioContext, "scenario");
        featureContextThreadLocal.get().getScenarioContexts().add(scenarioContext);

    }

    private synchronized void afterTest(TestRunFinished event) {
        featureContextThreadLocal.get().setStatus(getFeatureStatus(featureContextThreadLocal.get().getScenarioContexts()));
        logger.info(() -> String.format("After Test: %n Feature[%s] %n Scenarios[%s] %n Status [%s]",
            featureContextThreadLocal.get().getFeatureName(),
            getScenarios(featureContextThreadLocal.get().getScenarioContexts()),
            featureContextThreadLocal.get().getStatus()
        ));

        LiveReportHelper.publishResults(featureContextThreadLocal.get(), "feature");
        featureContextThreadLocal.remove();
        scenarioContextThreadLocal.remove();
    }

    private String getScenarios(List<ScenarioContext> scenarioContexts) {

        return scenarioContexts.stream().filter(s -> s.toString() != null).map(String::valueOf).collect(Collectors.joining("\n", "{", "}"));
    }

    private String getFeatureStatus(List<ScenarioContext> scenarioContexts) {

        Optional<ScenarioContext> contextOptional = scenarioContexts.stream()
            .filter(scenarioContext -> scenarioContext.getStatus().equalsIgnoreCase("FAILED")).findFirst();
        return contextOptional.isPresent() ? contextOptional.get().getStatus() : "PASSED";
    }

    @Builder
    @Data
    static class StepContext {
        String stepName;
        String status;
        String duration;
    }

    @Builder
    @Data
    static class FeatureContext {
        String featureName;
        String status;
        List<ScenarioContext> scenarioContexts;
    }

    @Builder
    @Data
    static class ScenarioContext {
        String scenarioName;
        String status;
        String duration;
        String error;

        @Override
        public String toString() {
            return "{scenarioName: " + this.getScenarioName() + ", status: " + this.getStatus() + ", duration:" + this.getDuration() + ", error:" + this.getError() + "}";
        }
    }

}