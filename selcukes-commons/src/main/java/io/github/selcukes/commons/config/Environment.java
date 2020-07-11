/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Environment {
    private String projectName;
    private String env;
    private String browserName;
    private String windowId;
    private Boolean headLess;
    private Boolean remote;
    private String isProxy;
    private String baseUrl;
    private String remoteGridUrl;
    private Boolean videoRecording;
    private Boolean notification;
    private Map<String, String> notifier;
}
