/*
 *
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
 *
 */

package io.github.selcukes.extent.report.support;

import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.exception.SnapshotException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.http.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Snapshot {
    private WebDriver driver;

    public Snapshot(WebDriver driver) {
        this.driver = driver;
    }

    public <X> X getFullScreenshotAs(OutputType<X> outputType) {
        defineCustomCommand(new CommandInfo("/session/:sessionId/chromium/send_command_and_get_result", HttpMethod.POST));
        Object metrics = sendEvaluate(
            "({" +
                "width: Math.max(window.innerWidth,document.body.scrollWidth,document.documentElement.scrollWidth)|0," +
                "height: Math.max(window.innerHeight,document.body.scrollHeight,document.documentElement.scrollHeight)|0," +
                "deviceScaleFactor: window.devicePixelRatio || 1," +
                "mobile: typeof window.orientation !== 'undefined'" +
                "})");
        sendCommand("Emulation.setDeviceMetricsOverride", metrics);
        Await.until(TimeUnit.MILLISECONDS, 500);
        Object result = sendCommand("Page.captureScreenshot", Map.of("format", "png", "fromSurface", true));
        sendCommand("Emulation.clearDeviceMetricsOverride", Map.of());
        String base64EncodedPng = (String) ((Map<String, ?>) result).get("data");
        return outputType.convertFromBase64Png(base64EncodedPng);
    }

    private void defineCustomCommand(CommandInfo info) {
        try {
            unwrapDriver();
            Method defineCommand = HttpCommandExecutor.class.getDeclaredMethod("defineCommand", String.class, CommandInfo.class);
            defineCommand.setAccessible(true);
            defineCommand.invoke(((RemoteWebDriver) this.driver).getCommandExecutor(), "sendCommand", info);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new SnapshotException(e);
        }
    }


    private Object sendCommand(String cmd, Object params) {
        try {
            Method execute = RemoteWebDriver.class.getDeclaredMethod("execute", String.class, Map.class);
            execute.setAccessible(true);
            Response res = (Response) execute.invoke(driver, "sendCommand", Map.of("cmd", cmd, "params", params));
            return res.getValue();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new SnapshotException(e);
        }
    }


    protected Object sendEvaluate(String script) {
        Object response = sendCommand("Runtime.evaluate", Map.of("returnByValue", true, "expression", script));
        Object result = ((Map<String, ?>) response).get("result");
        return ((Map<String, ?>) result).get("value");
    }

    private void unwrapDriver() {
        String[] wrapperClassNames = {"org.openqa.selenium.WrapsDriver", "org.openqa.selenium.internal.WrapsDriver"};
        for (String wrapperClassName : wrapperClassNames) {
            try {
                Class<?> clazz = Class.forName(wrapperClassName);
                if (clazz.isInstance(driver)) {
                    driver = (WebDriver) clazz.getMethod("getWrappedDriver").invoke(driver);
                }
            } catch (ReflectiveOperationException ignored) {
                //Gobble exception
            }
        }
    }
}
