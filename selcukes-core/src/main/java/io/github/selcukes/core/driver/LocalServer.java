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

package io.github.selcukes.core.driver;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.net.PortProber;

import java.util.Arrays;

@CustomLog
@UtilityClass
public class LocalServer {
    private static boolean isRunning = false;
    static int hubPort;

    public static void startSelenium(DriverType... driverType) {
        logger.info(() -> "Starting Selenium Server ...");
        Arrays.stream(driverType).distinct().forEach(BrowserOptions::setBinaries);
        hubPort = PortProber.findFreePort();
        if (isSeleniumServerNotRunning()) {
            logger.debug(() -> "Using Free Hub Port: " + hubPort);
            Main.main(new String[]{"standalone", "--port", String.valueOf(hubPort)});
            isRunning = true;
            logger.info(() -> "Selenium Server started...");
        }
    }

    static boolean isCloud() {
        return ConfigFactory.getConfig().getWeb().get("remote")
            .equalsIgnoreCase("true");
    }

    static boolean isSeleniumServerNotRunning() {
        return isCloud() && !LocalServer.isRunning;
    }

    static boolean isAppiumServerRunning() {
        return !ConfigFactory.getConfig().getMobile().get("remote")
            .equalsIgnoreCase("true") && LocalServer.isRunning;
    }

    public static void startAppium() {
        AppiumEngine.getInstance().startLocalServer();
        LocalServer.isRunning = true;
    }

    public static void stopAppium() {
        AppiumEngine.getInstance().stopServer();
    }

}
