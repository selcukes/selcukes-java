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

package io.github.selcukes.core.grid;

import io.github.selcukes.commons.http.WebClient;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.net.URL;
import java.time.Duration;
import java.util.Map;

@UtilityClass
public class SeleniumServer {
    private static boolean started;
    private static SeleniumService hub;

    public synchronized URL start(String mode, String... extraFlags) {
        if (started) {
            return hub.getServiceUrl();
        }
        hub = new SeleniumService().start(mode, extraFlags);

        WebClient client = new WebClient(hub.getServiceUrl().toString() + "/status");
        Wait<WebClient> wait = new FluentWait<>(client)
                .ignoring(RuntimeException.class)
                .withTimeout(Duration.ofSeconds(30));
        Json json = new Json();
        wait.until(c -> {
            var response = client.get().body();
            Map<?, ?> value = json.toType(response, Map.class);
            return Boolean.TRUE.equals(((Map<?, ?>) value.get("value")).get("ready"));
        });

        started = true;
        return hub.getServiceUrl();
    }
}
