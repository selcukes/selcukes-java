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

package io.github.selcukes.commons.config;

import io.github.selcukes.databind.annotation.DataFile;
import lombok.Data;

import java.util.Map;

@Data
@DataFile(fileName = "selcukes.yaml", streamLoader = true)
public class Config {
    private String projectName;
    private String env;
    private String baseUrl;
    private Map<String, String> excel;
    private Map<String, String> cucumber;
    private WebConfig web;
    private WindowsConfig windows;
    private MobileConfig mobile;
    private Map<String, String> reports;
    private VideoConfig video;
    private NotifierConfig notifier;

    @Data
    public static class MobileConfig {
        boolean remote;
        String cloud;
        String platform;
        String browser;
        String serviceUrl;
        String app;
    }

    @Data
    public static class WebConfig {
        boolean remote;
        String cloud;
        String browser;
        boolean headLess;
        String serviceUrl;
        String app;
    }

    @Data
    public static class WindowsConfig {
        String serviceUrl;
        String app;
    }

    @Data
    public static class NotifierConfig {
        boolean notification;
        String type;
        String webhookToken;
        String apiToken;
        String channel;
        String authorIcon;
    }

    @Data
    public static class VideoConfig {
        boolean recording;
        String type;
        String ffmpegPath;
        boolean watermark;
    }
}
