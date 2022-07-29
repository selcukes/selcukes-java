/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.reports.cucumber;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EmbedEvent;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.StepArgument;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import io.cucumber.plugin.event.WriteEvent;
import io.github.selcukes.extent.report.TestSourcesModel;
import lombok.CustomLog;

import java.util.Optional;

@CustomLog
public class CucumberListener implements ConcurrentEventListener {
    private final TestSourcesModel testSources = new TestSourcesModel();
    private CucumberService cucumberService;

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::beforeTest);
        publisher.registerHandlerFor(TestSourceRead.class, this::getTestSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, this::beforeScenario);
        publisher.registerHandlerFor(TestStepStarted.class, this::beforeStep);
        publisher.registerHandlerFor(TestStepFinished.class, this::afterStep);
        publisher.registerHandlerFor(TestCaseFinished.class, this::afterScenario);
        publisher.registerHandlerFor(TestRunFinished.class, this::afterTest);
        publisher.registerHandlerFor(EmbedEvent.class, getEmbedEventHandler());
        publisher.registerHandlerFor(WriteEvent.class, getWriteEventHandler());
    }

    private void getTestSourceReadHandler(TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.getUri(), event);
    }

    private void beforeTest(TestRunStarted event) {
        cucumberService = EventFiringCucumber.getService();
        cucumberService.beforeTest();
    }


    private void beforeScenario(TestCaseStarted event) {
        cucumberService.beforeScenario();
    }

    private void beforeStep(TestStepStarted event) {
        cucumberService.beforeStep();
    }

    private void afterStep(TestStepFinished event) {
        StringBuilder stepsReport = new StringBuilder();
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            stepsReport.append("Cucumber Step Failed : ")
                    .append(testStep.getStep().getText()).append("  [")
                    .append(testStep.getStep().getLine()).append("] ");
            Optional<StepArgument> stepsArgs = Optional.ofNullable(testStep.getStep().getArgument());
            if (stepsArgs.isPresent()) {
                stepsReport.append("Step Argument: [").append(stepsArgs).append("] ");
            }
        }
        cucumberService.afterStep(stepsReport.toString(), event.getResult().getStatus().is(Status.FAILED));
    }


    private void afterScenario(TestCaseFinished event) {
        cucumberService.afterScenario(event.getTestCase().getName(), event.getResult().getStatus());
    }

    private void afterTest(TestRunFinished event) {
        logger.trace(() -> String.format("After Test: %n Event [%s]",
                event.toString()
        ));

    }

    private EventHandler<EmbedEvent> getEmbedEventHandler() {
        return event ->
                logger.trace(() -> String.format("Embed Event: [%s]", event.getName()));

    }

    private EventHandler<WriteEvent> getWriteEventHandler() {
        return event ->
                logger.trace(() -> String.format("Write Event: [%s]", event.getText()));

    }

}
