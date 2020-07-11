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

package io.github.selcukes.reports.listeners;

import io.github.selcukes.core.logging.LogRecordListener;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.reports.enums.RecorderType;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.RecorderFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

public class ScreenPlayListener extends SelcukesTestNGListener {
    private Recorder recorder;
    private LogRecordListener logRecordListener;

    @Override
    public void onTestStart(ITestResult result) {
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
        recorder = RecorderFactory.getRecorder(RecorderType.FFMPEG);
        recorder.start();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        recorder.stopAndDelete();
        Reporter.log(getLogs(Level.INFO));
        LoggerFactory.removeListener(logRecordListener);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        File video = recorder.stopAndSave(result.getName().replace(" ", "_"));
        attachVideo(video.getAbsolutePath());
        Reporter.log(getLogs(Level.FINE));
        LoggerFactory.removeListener(logRecordListener);
    }

    private String getLogs(Level level) {
        return logRecordListener.getLogRecords(level)
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("<br/>  --> ", "<br/> Logs: <br/>   --> ", "<br/> --End Of Logs--"));

    }

    private void attachVideo(String videoPath) {
        String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" +
            "<source src=" + videoPath + " type=\"video/mp4\">" +
            "Your browser does not support the video tag." +
            "</video>";
        Reporter.log(htmlToEmbed);
    }

}
