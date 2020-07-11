/*
 *
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
 *
 */

package io.github.selcukes.reports.cucumber;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;

import java.util.Optional;

public class Selcukes implements ConcurrentEventListener {
    private final Logger logger = LoggerFactory.getLogger(Selcukes.class);
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
        logger.trace(() -> String.format("TestSource Test: %n  Source [%s] URI [%s]",
            event.getSource(),
            event.getUri()
        ));
    }

    private void beforeTest(TestRunStarted event) {
        cucumberService = EventFiringCucumber.getService();
        logger.trace(() -> String.format("Before Test: %n Event[%s]",
            event.toString()

        ));
        cucumberService.beforeTest();
    }


    private void beforeScenario(TestCaseStarted event) {
        logger.debug(() -> String.format("Before Scenario: %n Scenario Name[%s] %n Keyword [%s] %n Steps [%s]",
            event.getTestCase().getName(),
            event.getTestCase().getKeyword(),
            event.getTestCase().getTestSteps().toString()
        ));
        cucumberService.beforeScenario();
    }

    private void beforeStep(TestStepStarted event) {
        logger.debug(() -> String.format("Before Step: [%s]", event.getTestStep().toString()));
        cucumberService.beforeStep();
    }

    private void afterStep(TestStepFinished event) {
        logger.debug(() -> String.format("After Step: [%s]",
            event.getTestStep().toString()
        ));
        StringBuilder stepsReport = new StringBuilder();
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();

            stepsReport.append("Cucumber Step Failed : ")
                .append(testStep.getStep().getText()).append("  [")
                .append(testStep.getStep().getLine()).append("] ");
            Optional<StepArgument> stepsArgs = Optional.ofNullable(testStep.getStep().getArgument());
            if (stepsArgs.isPresent()) stepsReport.append("Step Argument: [").append(stepsArgs).append("] ");
        }
        cucumberService.afterStep(stepsReport.toString(), event.getResult().getStatus().is(Status.FAILED));
    }


    private void afterScenario(TestCaseFinished event) {
        logger.debug(() -> String.format("After Scenario: %n Status [%s] %n Duration [%s] %n Error [%s]",
            event.getResult().getStatus(),
            event.getResult().getDuration(),
            event.getResult().getError().getMessage()
        ));
        cucumberService.afterScenario(event.getTestCase().getName(), event.getResult().getStatus().is(Status.FAILED));
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