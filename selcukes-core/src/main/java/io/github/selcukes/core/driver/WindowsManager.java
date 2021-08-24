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

package io.github.selcukes.core.driver;

import io.appium.java_client.windows.WindowsDriver;
import io.github.selcukes.commons.helper.ExceptionHelper;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;


public class WindowsManager {
    private static final Logger logger = LoggerFactory.getLogger(WindowsManager.class);
    private static final String WINAPP_DRIVER_PATH = "C:/Program Files (x86)/Windows Application Driver/WinAppDriver.exe";
    private static WindowsDriver session;
    private static Process winProcess;
    private static Optional<String> defaultHubUrl = Optional.empty();
    private static String localServiceUrl = "http://127.0.0.1:";
    private static String defaultPort = "4723";

    public synchronized WindowsDriver createDriver() {
        if (null == session) {
            startWinAppDriver();
            try {

                session = new WindowsDriver<>(Objects.requireNonNull(getServiceUrl()), setCapabilities(""));
            } finally {
                logger.info(() -> "Closing Windows App...");
                Runtime.getRuntime().addShutdownHook(new Thread(new WinAppCleanup()));
            }
        }
        return session;
    }

    private void destroyDriver() {
        createDriver().quit();
        killWinAppDriver();
        session = null;
    }

    private void startWinAppDriver() {

        ProcessBuilder processBuilder = new ProcessBuilder(WINAPP_DRIVER_PATH);
        processBuilder.inheritIO();
        try {
            winProcess = processBuilder.start();
        } catch (IOException e) {
            ExceptionHelper.rethrow(e);
        }

        logger.info(() -> "WinAppDriver started...");
    }

    private void killWinAppDriver() {
        winProcess.destroy();
        logger.info(() -> "WinAppDriver killed...");
    }

    public WindowsDriver switchWindow(String name) {

        WebElement newWindowElement = session.findElementByName(name);
        String windowId = newWindowElement.getAttribute("NativeWindowHandle");
        System.out.println("Window Id: " + windowId + "After: " + Integer.toHexString(Integer.parseInt(windowId)));
        session = new WindowsDriver<>(getServiceUrl(), setCapabilities(Integer.toHexString(Integer.parseInt(windowId))));
        session.switchTo().activeElement();
        return session;
    }

    private URL getServiceUrl() {
        StringBuilder urlBuilder = new StringBuilder();
        try {
            if (defaultHubUrl.isPresent()) {
                logger.info(() -> "Using default URL " + defaultHubUrl.get());
                urlBuilder.append(defaultHubUrl.get())
                    .append(defaultPort);
            } else {
                logger.info(() -> "Using local server URL " + localServiceUrl);
                urlBuilder.append(localServiceUrl)
                    .append(getPortNumber());
            }
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {

            logger.error(() -> String.format("URL analysis failed with exception: %s", e));
        }

        return null;
    }

    private String getPortNumber() {
        String portNumber = System.getProperty("window.port.number");
        if (portNumber == null)
            portNumber = defaultPort;
        return portNumber;
    }


    public DesiredCapabilities setCapabilities(String windowId) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("appTopLevelWindow", windowId);
        return capabilities;
    }

    public class WinAppCleanup implements Runnable {
        public void run() {
            logger.info(() -> "Closing the Windows App");
            try {
                destroyDriver();
            } catch (Exception x) {
                logger.info(() -> "Cannot close Windows App");

            }
        }
    }


}
