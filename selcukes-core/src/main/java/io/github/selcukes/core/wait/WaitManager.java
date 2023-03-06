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

package io.github.selcukes.core.wait;

import io.github.selcukes.commons.Await;
import io.github.selcukes.core.page.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WaitManager {
    Page page;

    public WaitManager(Page page) {
        this.page = page;
        waitForJQueryLoadIfDefined();
        waitForAngularIfDefined();

    }

    private void waitForJQueryLoadIfDefined() {
        Boolean jQueryDefined = page.executeScript("return typeof jQuery != 'undefined'");
        if (Boolean.TRUE.equals(jQueryDefined)) {
            ExpectedCondition<Boolean> jQueryLoad = null;
            try {
                // Wait for jQuery to load
                jQueryLoad = driver -> ((Long) page.executeScript("return jQuery.active") == 0);
            } catch (NullPointerException e) {
                // do nothing
            }
            // Get JQuery is Ready
            boolean jqueryReady = page.executeScript("return jQuery.active==0");

            if (!jqueryReady) {
                // Wait JQuery until it is Ready!
                int tryCounter = 0;
                while ((!jqueryReady) && (tryCounter < 5)) {
                    try {
                        // Wait for jQuery to load
                        page.waitFor(jQueryLoad, Page.TIMEOUT);
                    } catch (NullPointerException e) {
                        // do nothing
                    }
                    sleep();
                    tryCounter++;
                    jqueryReady = page.executeScript("return jQuery.active == 0");
                }
            }
        }
    }

    private void waitForAngularLoad() {

        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        // Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = driver -> Boolean
                .valueOf(
                    ((JavascriptExecutor) Objects.requireNonNull(driver)).executeScript(angularReadyScript).toString());

        // Get Angular is Ready
        boolean angularReady = Boolean.parseBoolean(page.executeScript(angularReadyScript).toString());

        if (!angularReady) {
            // Wait ANGULAR until it is Ready!
            int tryCounter = 0;
            while ((!angularReady) && (tryCounter < 5)) {
                // Wait for Angular to load
                page.waitFor(angularLoad, Page.TIMEOUT);
                sleep();
                tryCounter++;
                angularReady = Boolean.parseBoolean(page.executeScript(angularReadyScript).toString());
            }
        }
    }

    private void waitForAngularIfDefined() {
        try {
            Boolean angularDefined = !((Boolean) page.executeScript("return window.angular === undefined"));
            if (Boolean.TRUE.equals(angularDefined)) {
                Boolean angularInjectorDefined = !((Boolean) page
                        .executeScript("return angular.element(document).injector() === undefined"));

                if (Boolean.TRUE.equals(angularInjectorDefined)) {
                    waitForAngularLoad();
                }
            }
        } catch (Exception e) {
            // do nothing
        }
    }

    private void sleep() {
        Await.until(TimeUnit.MILLISECONDS, 20);
    }

}
