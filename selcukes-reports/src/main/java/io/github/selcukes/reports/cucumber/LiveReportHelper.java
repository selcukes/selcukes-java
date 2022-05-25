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

package io.github.selcukes.reports.cucumber;

import io.github.selcukes.commons.http.Response;
import io.github.selcukes.commons.http.WebClient;
import lombok.CustomLog;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

@CustomLog
@UtilityClass
public class LiveReportHelper {
    @SneakyThrows
    public <T> void publishResults(T json, String key) {
        String url = "http://localhost:9200/%s/results";
        WebClient client = new WebClient(String.format(url, key));
        Response response = client.post(json);
        String responseString = IOUtils.toString(response.getResponseStream(), StandardCharsets.UTF_8);
        logger.debug(() -> responseString);
    }
}
