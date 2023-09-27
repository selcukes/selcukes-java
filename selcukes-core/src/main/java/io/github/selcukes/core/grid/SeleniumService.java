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

import io.github.selcukes.collections.Resources;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.DriverConnectionException;
import io.github.selcukes.commons.helper.FileHelper;
import lombok.CustomLog;
import lombok.SneakyThrows;
import org.openqa.selenium.Platform;
import org.openqa.selenium.net.NetworkUtils;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.os.CommandLine;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;

@CustomLog
class SeleniumService {
    private static final List<Runnable> SHUTDOWN_ACTIONS = new LinkedList<>();
    private String baseUrl;
    private CommandLine command;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> SHUTDOWN_ACTIONS.forEach(Runnable::run)));
    }

    /**
     * Creates Selenium Server process.
     *
     * @param  mode                      server mode to start
     * @param  extraFlags                extra flags to start the server with
     * @return                           the new selenium server instance
     * @throws DriverConnectionException if server is already started or fails
     *                                   to start
     */
    public SeleniumService start(String mode, String... extraFlags) {
        if (command != null) {
            throw new DriverConnectionException("Server already started");
        }

        String serverJar = getServerJar();
        int port = PortProber.findFreePort();
        String localAddress = new NetworkUtils().getPrivateLocalAddress();
        baseUrl = String.format("http://%s:%d", localAddress, port);

        Stream<String> javaFlags = System.getProperties().entrySet().stream()
                .filter(entry -> {
                    String key = String.valueOf(entry.getKey());
                    return key.startsWith("selenium") || key.startsWith("webdriver");
                })
                .map(entry -> "-D" + entry.getKey() + "=" + entry.getValue());

        command = new CommandLine("java", Stream.concat(javaFlags, Stream.concat(
            Stream.of("-jar", serverJar, mode, "--port", String.valueOf(port)),
            Stream.of(extraFlags))).toArray(String[]::new));

        if (Platform.getCurrent().is(Platform.WINDOWS)) {
            File workingDir = new File(".");
            command.setWorkingDirectory(workingDir.getAbsolutePath());
        }
        command.copyOutputTo(System.err);
        logger.info(() -> "Starting selenium server: " + command.toString());
        command.executeAsync();

        try {
            URL url = new URL(baseUrl + "/status");
            new UrlChecker().waitUntilAvailable(10, SECONDS, url);
            logger.info(() -> "Selenium Server is ready...");
        } catch (UrlChecker.TimeoutException e) {
            logger.error(() -> "Server failed to start: " + e.getMessage());
            command.destroy();
            command = null;
            throw new DriverConnectionException(e);
        } catch (MalformedURLException e) {
            throw new DriverConnectionException(e);
        }

        SHUTDOWN_ACTIONS.add(this::stop);
        return this;
    }

    /**
     * Stops the selenium server instance.
     */
    public void stop() {
        if (command != null) {
            command.destroy();
            logger.info(() -> "Selenium Server stopped.");
            command = null;
        }
    }

    /**
     * Returns the base URL of the selenium service.
     *
     * @return the base URL of the selenium service
     */
    @SneakyThrows
    public URL getServiceUrl() {
        return new URL(baseUrl);
    }

    /**
     * Downloads and returns the path to the selenium server jar file.
     *
     * @return the path to the selenium server jar file
     */

    @SneakyThrows
    public String getServerJar() {
        var seleniumServerJar = ConfigFactory.getConfig().getWeb().getServerJar();
        var serverJarUrl = new URL(seleniumServerJar);
        Path serverJarPath = Resources.of("target/selenium-server.jar");
        FileHelper.download(serverJarUrl, serverJarPath.toFile());
        return serverJarPath.toAbsolutePath().toString();
    }
}
