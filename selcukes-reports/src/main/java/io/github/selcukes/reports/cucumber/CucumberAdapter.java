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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.logging.LogRecordListener;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.notifier.Notifier;
import io.github.selcukes.notifier.NotifierFactory;
import io.github.selcukes.reports.html.HtmlReporter;
import io.github.selcukes.video.Recorder;
import io.github.selcukes.video.RecorderFactory;

import java.io.File;

import static io.github.selcukes.commons.config.ConfigFactory.getConfig;

public class CucumberAdapter implements CucumberService {
    private Recorder recorder;
    private LogRecordListener logRecordListener;
    private Notifier notifier;
    private String stepInfo;
    private boolean isRecordingEnabled;
    private boolean isNotifierEnabled;

    @Override
    public void beforeTest() {
        isRecordingEnabled = ConfigFactory.getConfig().getVideo().get("recording").equalsIgnoreCase("true");
        isNotifierEnabled = ConfigFactory.getConfig().getNotifier().get("notification").equalsIgnoreCase("true");
        if (isRecordingEnabled)
            recorder = RecorderFactory.getRecorder();
        if (isNotifierEnabled)
            notifier = NotifierFactory.getNotifier();
    }

    @Override
    public void beforeScenario() {
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
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

    }

    @Override
    public void afterScenario(String scenarioName, boolean status) {
        String path = "";
        if (isRecordingEnabled) {
            if (status) {
                File video = recorder.stopAndSave(scenarioName.replace(" ", "_"));
                path = video.toURI().toString();
            } else
                recorder.stopAndDelete();
        }
        if (isNotifierEnabled) {
            notifier.scenarioName(scenarioName)
                .scenarioStatus("FAILED")
                .stepDetails(stepInfo)
                .path(path)
                .pushNotification();
        }
        LoggerFactory.removeListener(logRecordListener);
    }

    @Override
    public void afterTest() {
        HtmlReporter.generateReports(getConfig().getReports().get("path"));
    }
}

