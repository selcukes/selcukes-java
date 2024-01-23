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
import org.openqa.selenium.support.ui.FluentWait;

import java.net.URL;
import java.time.Duration;

@UtilityClass
public class SeleniumServer {
    private static boolean started;
    private static SeleniumService hub;

    public synchronized URL start(String mode, String... extraFlags) {
        if (started) {
            return hub.getServiceUrl();
        }
        hub = new SeleniumService().start(mode, extraFlags);

        var webClient = new WebClient(hub.getServiceUrl().toString() + "/status");
        var wait = new FluentWait<>(webClient)
                .ignoring(RuntimeException.class)
                .pollingEvery(Duration.ofSeconds(1))
                .withTimeout(Duration.ofSeconds(30));
        wait.until(client -> {
            try {
                return client.get().bodyJson().at("/value/ready").asBoolean();
            } catch (Exception e) {
                return false;
            }
        });

        started = true;
        return hub.getServiceUrl();
    }
}
