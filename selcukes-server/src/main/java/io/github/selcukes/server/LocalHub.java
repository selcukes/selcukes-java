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

package io.github.selcukes.server;

import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.web.Hub;

public class LocalHub implements Bootable {

    private Hub hub;

    public LocalHub(GridHubConfiguration cfg) {
        hub = new Hub(cfg);
    }

    @Override
    public void start() {
        hub.start();
    }

    public boolean isNodeRegistered() {
        return !hub.getRegistry().getAllProxies().isEmpty();
    }

    @Override
    public void stop() {
        hub.stop();
    }
}
