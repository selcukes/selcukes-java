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

package io.github.selcukes.core.tests.unit;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.enums.DeviceType;
import io.github.selcukes.core.page.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class ShadowRootTest {
    @Test
    public void shadowElementTest() {
        ConfigFactory.getConfig().getWeb().setRemote(false);
        WebDriver driver = DriverManager.createDriver(DeviceType.BROWSER);
        WebPage page = new WebPage(driver);

        page.open("http://watir.com/examples/shadow_dom.html");

        WebElement shadowContent = page.findShadowChild(By.cssSelector("#shadow_host"), By.cssSelector("#shadow_content"));
        page.assertThat().element(shadowContent).textAs("some text");

    }
}
