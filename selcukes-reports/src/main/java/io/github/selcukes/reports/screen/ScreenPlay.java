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

package io.github.selcukes.reports.screen;

import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.enums.RecorderType;

import java.io.File;

public interface ScreenPlay {
    /**
     * Capture Screenshot
     */
    String takeScreenshot();

    /**
     * Attach Full page screenshot to cucumber report
     * scenario.embed(objToEmbed, mediaType, scenario.getName());
     */
    ScreenPlay attachScreenshot();

    /**
     * Start Video Recorder
     */
    ScreenPlay start();

    /**
     * Attach Video on Failure to cucumber report
     * scenario.embed(objToEmbed, mediaType, scenario.getName());
     */
    ScreenPlay attachVideo();


    /**
     * Stop Video Recorder
     */
    File stop();

    ScreenPlay sendNotification(String step);

    /**
     * Attach INFO Level Logs to cucumber report
     * scenario.embed(objToEmbed, mediaType);
     */
    void attachLogs();

    /**
     * Get Recorder object by Type
     */
    ScreenPlay withRecorder(RecorderType recorderType);

    /**
     * Get Notifier object by Type
     */
    ScreenPlay withNotifier(NotifierType notifierType);

    /**
     * Read Test Result from result object
     */
    <T> ScreenPlay withResult(T result);

    ScreenPlay ignoreCondition();
}
