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

import io.cucumber.plugin.event.Status;
import io.github.selcukes.notifier.Notifier;
import io.github.selcukes.notifier.NotifierFactory;
import io.github.selcukes.reports.html.HtmlReporter;
import io.github.selcukes.video.Recorder;
import io.github.selcukes.video.RecorderFactory;

import java.io.File;

import static io.github.selcukes.commons.config.ConfigFactory.getConfig;
import static io.github.selcukes.extent.report.Reporter.getReporter;

public class CucumberAdapter implements CucumberService {
    private Recorder recorder;

    private Notifier notifier;
    private String stepInfo;
    private boolean isRecordingEnabled;
    private boolean isNotifierEnabled;

    @Override
    public void beforeTest() {
        isRecordingEnabled = getConfig().getVideo().isRecording();
        isNotifierEnabled = getConfig().getNotifier().isNotification();
        if (isRecordingEnabled)
            recorder = RecorderFactory.getRecorder();
        if (isNotifierEnabled)
            notifier = NotifierFactory.getNotifier();
    }

    @Override
    public void beforeScenario() {
        if (isRecordingEnabled)
            recorder.start();
    }

    @Override
    public void beforeStep() {
        // Nothing required
    }

    @Override
    public void afterStep(String step, boolean status) {
        if (status) stepInfo = step;
        getReporter().getLogRecords();

    }

    @Override
    public void afterScenario(String scenarioName, Status status) {
        String path = "";
        if (isRecordingEnabled) {
            if (status.is(Status.FAILED)) {
                File video = recorder.stopAndSave(scenarioName.replace(" ", "_"));
                path = video.toURI().toString();
            } else
                recorder.stopAndDelete();
        }
        if (isNotifierEnabled) {
            notifier.scenarioName(scenarioName)
                .scenarioStatus(status.name())
                .stepDetails(stepInfo)
                .path(path)
                .pushNotification();
        }
    }

    @Override
    public void afterTest() {
        HtmlReporter.generateReports(getConfig().getReports().get("path"));
    }
}

