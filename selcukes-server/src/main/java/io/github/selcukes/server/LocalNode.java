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

import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.selenium.remote.server.SeleniumServer;

import java.util.concurrent.TimeUnit;

public class LocalNode implements Bootable {

    private SelfRegisteringRemote node;
    private LocalHub hub;

    public LocalNode(LocalHub hub, GridNodeConfiguration cfg) {
        this.hub = hub;
        node = new SelfRegisteringRemote(cfg);
        SeleniumServer server = new SeleniumServer(node.getConfiguration());
        node.setRemoteServer(server);
    }

    @Override
    public void start() {
        if (node.startRemoteServer()) {
            node.sendRegistrationRequest();
        }
        int attempt = 1;
        boolean registered = false;
        while (attempt++ <= 10) {
            if (hub.isNodeRegistered()) {
                registered = true;
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (!registered) {
            throw new IllegalStateException("Node registration failed");
        }
    }

    @Override
    public void stop() {
        node.stopRemoteServer();
    }
}
