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

package io.github.selcukes.reports.screen;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

public class ScreenPlayTestNG extends ScreenPlayImpl {
    private ITestResult result;

    public ScreenPlayTestNG(WebDriver driver) {
        super(driver);
    }

    @Override
    public <T> ScreenPlay readTest(T result) {
        this.result = (ITestResult) result;
        return this;
    }

    @Override
    public void attachScreenshot() {
        Reporter.log("<br><img src='" + takeScreenshot() + "' height='300' width='300'/><br>");
    }

    @Override
    public void attachVideo() {

        String videoPath = recorder.stopAndSave(result.getName().replace(" ", "_")).getAbsolutePath();

        String htmlToEmbed = "<video width=\"864\" height=\"576\" controls>" +
            "<source src=" + videoPath + " type=\"video/mp4\">" +
            "Your browser does not support the video tag." +
            "</video>";
        Reporter.log(htmlToEmbed);

    }

    @Override
    public ScreenPlay sendNotification(String message) {
        notifier.pushNotification(result.getName(), getTestStatus(), message, takeScreenshot());
        return this;
    }
    @Override
    public void attachLogs() {
        String infoLogs = loggerListener.getLogRecords(Level.INFO)
            .map(LogRecord::getMessage)
            .collect(Collectors.joining("\n  --> ", "\n--Info Logs-- \n\n  --> ", "\n\n--End Of Logs--"));
        Reporter.log(infoLogs);
        stopReadingLogs();
    }
    private String getTestStatus() {
        String status = null;
        if (result.isSuccess()) {
            status = "PASS";
        } else if (result.getStatus() == ITestResult.FAILURE) {
            status = "FAILED";
        } else {
            status = "SKIPPED";
        }
        return status;
    }
}
