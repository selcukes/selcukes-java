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

package io.github.selcukes.core.tests.mobile;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.github.selcukes.commons.annotation.Lifecycle;
import io.github.selcukes.core.page.MobilePage;
import io.github.selcukes.core.page.Pages;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

@Lifecycle
public class AndroidCalculatorTest {
    MobilePage page;

    @BeforeMethod
    void beforeTest() {
        var options = new UiAutomator2Options();
        options.setAppPackage("com.android.calculator2");
        options.setAppActivity("com.android.calculator2.Calculator");
        options.setNewCommandTimeout(Duration.ofSeconds(11));
        options.setFullReset(false);
        page = Pages.mobilePage(options);
    }

    @Test(enabled = false)
    void calculator() {
        page.click(By.id("digit_2"))
                .click(By.id("op_add"))
                .click(By.id("digit_4"))
                .click(By.id("eq"))
                .assertThat().element(By.id("result")).textAs("6");
    }
}
