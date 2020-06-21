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
import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.VideoRecorder;

import java.util.Optional;


public class Selcukes implements ConcurrentEventListener {
    private final Logger logger = LoggerFactory.getLogger(Selcukes.class);
    private final TestSourcesModel testSources = new TestSourcesModel();
    private Recorder recorder;
    private LogRecordListener logRecordListener;

    /**
     * Registers an event handler for a specific event.
     * <p>
     * The available events types are:
     * <ul>
     * <li>{@link TestRunStarted} - the first event sent.
     * <li>{@link TestSourceRead} - sent for each feature file read, contains the feature file source.
     * <li>{@link TestCaseStarted} - sent before starting the execution of a Test Case(/Pickle/Scenario), contains the Test Case
     * <li>{@link TestStepStarted} - sent before starting the execution of a Test Step, contains the Test Step
     * <li>{@link TestStepFinished} - sent after the execution of a Test Step, contains the Test Step and its Result.
     * <li>{@link TestCaseFinished} - sent after the execution of a Test Case(/Pickle/Scenario), contains the Test Case and its Result.
     * <li>{@link TestRunFinished} - the last event sent.
     * <li>{@link EmbedEvent} - calling scenario.embed in a hook triggers this event.
     * <li>{@link WriteEvent} - calling scenario.write in a hook triggers this event.
     * </ul>
     */
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

        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
    }


    private void beforeScenario(TestCaseStarted event) {
        if (ConfigFactory.getConfig().getVideoRecording()) {
            recorder = VideoRecorder.monteRecorder();
            recorder.start();
        }
    }

    private void beforeStep(TestStepStarted event) {
        logger.trace(() -> String.format("Before Step: [%s]", event.getTestStep().toString()));

    }

    private void afterStep(TestStepFinished event) {
        logger.trace(() -> String.format("After Step: [%s]", event.getTestStep().toString()));
        if (event.getResult().getStatus().is(Status.FAILED) && event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            StringBuilder stepsReport = new StringBuilder();
            stepsReport.append("Cucumber Step Failed : ")
                .append(testStep.getStep().getText()).append("  [")
                .append(testStep.getStep().getLine()).append("] ");
            Optional<StepArgument> stepsArgs = Optional.ofNullable(testStep.getStep().getArgument());
            if (stepsArgs.isPresent()) stepsReport.append("Step Argument: [").append(stepsArgs).append("] ");
            logger.debug(stepsReport::toString);
        }

    }


    private void afterScenario(TestCaseFinished event) {
        if (ConfigFactory.getConfig().getVideoRecording()) {
            if (event.getResult().getStatus().is(Status.FAILED))
                recorder.stopAndSave(event.getTestCase().getName());
            else
                recorder.stopAndDelete(event.getTestCase().getName());
        }
    }

    private void afterTest(TestRunFinished event) {

        LoggerFactory.removeListener(logRecordListener);
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
