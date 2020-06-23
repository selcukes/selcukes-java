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

import io.github.selcukes.reports.enums.NotifierType;
import io.github.selcukes.reports.enums.RecorderType;

public interface ScreenPlay {
    /**
     * Capture Screenshot
     */
    String takeScreenshot();

    /**
     * Embed Full page screenshot to cucumber report
     * scenario.embed(objToEmbed, mediaType);
     */
    void embedScreenshot();

    /**
     * Attach Full page screenshot to cucumber report
     * scenario.embed(objToEmbed, mediaType, scenario.getName());
     */
    void attachScreenshot();

    /**
     * Start Video Recorder
     */
    ScreenPlay start();

    /**
     * Attach Video on Failure to cucumber report
     * scenario.embed(objToEmbed, mediaType, scenario.getName());
     */
    void attachVideo();

    /**
     * Embed Video on Failure to cucumber report
     * scenario.embed(objToEmbed, mediaType);
     */
    void embedVideo();

    /**
     * Stop Video Recorder
     */
    ScreenPlay stop();

    ScreenPlay sendNotification(String step);

    /**
     * Attach INFO Level Logs to cucumber report
     * scenario.embed(objToEmbed, mediaType);
     */
    void attachLogs();

    /**
     * Get Recorder object by Type
     */
    ScreenPlay getRecorder(RecorderType recorderType);

    /**
     * Get Notifier object by Type
     */
    ScreenPlay getNotifier(NotifierType notifierType);

}
