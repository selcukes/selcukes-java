package io.github.selcukes.reports.cucumber;


import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.VideoRecorder;

import java.util.Optional;


public class Selcukes implements ConcurrentEventListener {
    private final Logger logger = LoggerFactory.getLogger(Selcukes.class);
    private final TestSourcesModel testSources = new TestSourcesModel();
    private Recorder recorder;
    private String scenarioName;


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


    }


    private void beforeScenario(TestCaseStarted event) {
        scenarioName = event.getTestCase().getName();
        if (ConfigFactory.getConfig().getVideoRecording()) {
            recorder = new VideoRecorder();
            recorder.start();
        }
    }

    private void beforeStep(TestStepStarted event) {


    }

    private void afterStep(TestStepFinished event) {

        if (event.getResult().getStatus().is(Status.FAILED)) {
            if (event.getTestStep() instanceof PickleStepTestStep) {
                PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
                StringBuilder stepsReport = new StringBuilder();
                stepsReport.append("Cucumber Step Failed : ").append(testStep.getStep().getText()).append("  [").append(testStep.getStep().getLine()).append("] ");
                Optional<StepArgument> stepsArgs = Optional.ofNullable(testStep.getStep().getArgument());
                if (stepsArgs.isPresent()) stepsReport.append("Step Argument: [").append(stepsArgs).append("] ");
                logger.debug(stepsReport::toString);
            }
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

    }

    private EventHandler<EmbedEvent> getEmbedEventHandler() {
        return event -> {
            // embedding(event.mimeType, event.data);
        };
    }

    private EventHandler<WriteEvent> getWriteEventHandler() {
        return event -> {
            // write(event.text);
        };
    }

}
