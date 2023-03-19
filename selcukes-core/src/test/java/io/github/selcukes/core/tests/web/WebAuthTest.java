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

package io.github.selcukes.core.tests.web;

import io.github.selcukes.core.page.WebPage;
import io.github.selcukes.core.wait.WaitCondition;
import lombok.CustomLog;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.github.selcukes.core.tests.TestDriver.getChromeDriver;

@CustomLog
public class WebAuthTest {
    WebPage page;
    WebDriver driver;

    @BeforeMethod
    private void setup() {
        driver = getChromeDriver();
        page = new WebPage(driver);
    }

    @AfterMethod
    private void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(enabled = false)
    public void testVirtualAuth() {
        page.open("https://webauthn.io/");
        page.addVirtualAuthenticator();

        String randomId = UUID.randomUUID().toString();
        logger.info(() -> "Username: " + randomId);
        page.enter(By.id("input-email"), randomId);
        page.click(By.id("register-button"));
        page.waitFor(ExpectedConditions.textToBePresentInElementLocated(
            By.className("popover-body"), "Success! Now try logging in"), 20);

        page.click(By.id("login-button"));
        page.waitFor(By.xpath("//*[contains(@class,'main-content')]//h3"), "You're logged in!",
            WaitCondition.TEXT_TO_BE);
        page.removeVirtualAuthenticator();

    }

    @Test
    public void testBasicAuth() {
        page.basicAuth("admin", "admin");
        page.open("https://the-internet.herokuapp.com/basic_auth");
        page.assertThat().title("The Internet");
    }
}
