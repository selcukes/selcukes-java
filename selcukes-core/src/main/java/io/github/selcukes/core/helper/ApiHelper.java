/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.core.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.selcukes.commons.http.WebClient;
import lombok.CustomLog;
import lombok.Data;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.util.Map;

@UtilityClass
@CustomLog
public class ApiHelper {

    public String getAppUrl(final Path apkPath) {
        logger.info(() -> "Uploading App to Browser Stack...");
        String user = System.getProperty("browserstack.user");
        String key = System.getProperty("browserstack.key");
        String serviceUrl = "https://api-cloud.browserstack.com/app-automate/upload";

        Map<Object, Object> data = Map.of("file", apkPath,
                "custom_id", "selcukes_" + apkPath.getFileName().toString());

        var client = new WebClient(serviceUrl);
        var response = client
                .authenticator(user, key)
                .multiPart(data)
                .post();
        var body = response.bodyAs(ResponseBody.class);
        return body.getAppUrl();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    class ResponseBody {
        String appUrl;
        String customId;
        String shareableId;
    }
}
