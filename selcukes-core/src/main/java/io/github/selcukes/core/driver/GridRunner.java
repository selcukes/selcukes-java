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
import io.github.selcukes.core.enums.DriverType;
import io.github.selcukes.wdb.WebDriverBinary;
import lombok.CustomLog;
import org.openqa.selenium.grid.Main;
import org.openqa.selenium.net.PortProber;

import java.util.Arrays;

@CustomLog
public class GridRunner {
    private static boolean isGridStarted = false;
    protected static int HUB_PORT;

    public static void startGrid(DriverType... driverType) {
        Arrays.stream(driverType).distinct().forEach(GridRunner::setBinaries);
        HUB_PORT = PortProber.findFreePort();
        if (ConfigFactory.getConfig().getWeb().get("remote").equalsIgnoreCase("true") && !isGridStarted) {
            Main.main(new String[]{"standalone", "--port", String.valueOf(HUB_PORT)});
            isGridStarted = true;
            logger.info(() -> "Grid Server started...");
        }

    }

    private static void setBinaries(DriverType driverType) {
        switch (driverType) {
            case EDGE:
                WebDriverBinary.edgeDriver().setup();
                break;
            case FIREFOX:
                WebDriverBinary.firefoxDriver().setup();
                break;
            case IEXPLORER:
                WebDriverBinary.ieDriver().setup();
                break;
            default:
                WebDriverBinary.chromeDriver().setup();

        }
    }
}
