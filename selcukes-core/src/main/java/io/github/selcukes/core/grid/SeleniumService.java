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

import io.github.selcukes.commons.exception.DriverSetupException;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.databind.utils.Resources;
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
    private static final List<Runnable> shutdownActions = new LinkedList<>();
    private String baseUrl;
    private CommandLine command;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownActions.forEach(Runnable::run)));
    }

    /**
     * Creates Selenium Server process.
     *
     * @return The new server.
     */
    public SeleniumService start(String mode, String... extraFlags) {
        logger.debug(() -> "Got a request to start a new selenium server");
        if (command != null) {
            logger.info(() -> "Server already started");
            throw new DriverSetupException("Server already started");
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
            logger.info(() -> "Waiting for server status on URL " + url);
            new UrlChecker().waitUntilAvailable(10, SECONDS, url);
            logger.info(() -> "Server is ready");
        } catch (UrlChecker.TimeoutException e) {
            logger.error(() -> "Server failed to start: " + e.getMessage());
            command.destroy();
            logger.error(() -> command.getStdOut());
            command = null;
            throw new DriverSetupException(e);
        } catch (MalformedURLException e) {
            throw new DriverSetupException(e);
        }
        shutdownActions.add(this::stop);

        return this;
    }

    public void stop() {
        if (command != null) {
            logger.info(() -> "Stopping selenium server");
            command.destroy();
            logger.info(() -> "Selenium server stopped");
            command = null;
        }
    }

    public URL getServiceUrl() {
        try {
            return new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new DriverSetupException(e);
        }
    }

    @SneakyThrows
    public String getServerJar() {
        var serverJarUrl = new URL(
            "https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.8.0/selenium-server-4.8.1.jar");
        Path serverJarPath = Resources.of("target/selenium-server.jar");
        FileHelper.download(serverJarUrl, serverJarPath.toFile());
        return serverJarPath.toAbsolutePath().toString();
    }
}
