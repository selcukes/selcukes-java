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

import io.cucumber.messages.types.Feature;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStepFinished;
import io.github.selcukes.extent.report.TestSourcesModel;
import lombok.Builder;
import lombok.CustomLog;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CustomLog
public class CucumberLiveReportAdapter implements ConcurrentEventListener {

    private final TestSourcesModel testSources = new TestSourcesModel();
    private final ThreadLocal<FeatureContext> featureContextThreadLocal = new InheritableThreadLocal<>();
    private final ThreadLocal<List<ScenarioContext>> scenarioContextThreadLocal = new InheritableThreadLocal<>();


    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, this::getTestSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, this::beforeScenario);
        publisher.registerHandlerFor(TestStepFinished.class, this::afterStep);
        publisher.registerHandlerFor(TestCaseFinished.class, this::afterScenario);
        publisher.registerHandlerFor(TestRunFinished.class, this::afterTest);
    }

    private synchronized void getTestSourceReadHandler(TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.getUri(), event);
    }

    private synchronized void beforeScenario(TestCaseStarted event) {
        Feature currentFeature = Objects.requireNonNull(testSources.getFeature(event.getTestCase().getUri()));

        if (featureContextThreadLocal.get() == null) {
            initFeatureContext(currentFeature);
        }

        if (!featureContextThreadLocal.get().getFeatureName().equalsIgnoreCase(currentFeature.getName())) {
            featureContextThreadLocal.get().setStatus(getFeatureStatus(featureContextThreadLocal.get().getScenarioContexts()));
            LiveReportHelper.publishResults(featureContextThreadLocal.get(), "feature");
            featureContextThreadLocal.remove();
            scenarioContextThreadLocal.remove();
            initFeatureContext(currentFeature);
        }
    }

    private synchronized void initFeatureContext(Feature currentFeature) {
        scenarioContextThreadLocal.set(new ArrayList<>(Collections.emptyList()));
        FeatureContext featureContext = FeatureContext.builder()
                .featureName(currentFeature.getName())
                .description(currentFeature.getDescription())
                .scenarioContexts(scenarioContextThreadLocal.get())
                .build();

        featureContextThreadLocal.set(featureContext);
    }

    private synchronized void afterStep(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
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
        LiveReportHelper.publishResults(featureContextThreadLocal.get(), "feature");
        featureContextThreadLocal.remove();
        scenarioContextThreadLocal.remove();
    }

    private synchronized String getFeatureStatus(List<ScenarioContext> scenarioContexts) {
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
        String description;
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
    }

}
