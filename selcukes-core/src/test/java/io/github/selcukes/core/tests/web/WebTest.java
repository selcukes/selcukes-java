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

import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.driver.GridRunner;
import io.github.selcukes.core.page.Pages;
import io.github.selcukes.core.page.WebPage;
import io.github.selcukes.wdb.enums.DriverType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class WebTest {
    @BeforeSuite
    public void beforeClass() {
        GridRunner.startSelenium(DriverType.CHROME);
    }

    @Test(enabled = false)
    public void remoteWebTest() {

        WebPage page = Pages.webPage();
        page.open("https://www.google.com/")
            .assertThat().title("Google");
    }

    @AfterMethod
    public void afterClass() {
        DriverManager.removeDriver();
    }
}
