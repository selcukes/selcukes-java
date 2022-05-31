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

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.driver.GridRunner;
import io.github.selcukes.core.enums.DeviceType;
import io.github.selcukes.core.enums.SwipeDirection;
import io.github.selcukes.core.page.MobilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class MobileAppTest {
    MobilePage page;

    @BeforeMethod
    void beforeTest() {
        GridRunner.startAppium();
        WebDriver driver = DriverManager.createDriver(DeviceType.MOBILE);
        page = new MobilePage(driver);

    }

    @Test(enabled = false)
    public void expandAndScrollScreenTest() {
        page.tap("Views")
            .tap("Expandable Lists")
            .tap("3. Simple Adapter")
            .swipe(By.xpath("//android.widget.TextView[@text='Group 18']"), SwipeDirection.DOWN)
            .tap(By.xpath("//android.widget.TextView[@text='Group 18']"))
            .swipe(By.xpath("//android.widget.TextView[@text='Child 13']"), SwipeDirection.DOWN)
            .swipe(By.xpath("//android.widget.TextView[@text='Group 1']"), SwipeDirection.UP);

    }

    @Test(enabled = false)
    public void expandAndScrollElementTest() {
        page.tap("Views")
            .swipe("Splitting Touches across Views", SwipeDirection.DOWN)
            .tap("Splitting Touches across Views")
            .swipe(By.id("io.appium.android.apis:id/list2"), By.xpath("//android.widget.ListView[2]/android.widget.TextView[@text='Blue']"), SwipeDirection.DOWN)
            .tap(By.xpath("//android.widget.ListView[2]/android.widget.TextView[@text='Blue']"))
            .swipe(By.id("io.appium.android.apis:id/list2"), By.xpath("//android.widget.ListView[2]/android.widget.TextView[@text='Abbaye de Belloc']"), SwipeDirection.UP)
            .tap(By.xpath("//android.widget.ListView[2]/android.widget.TextView[@text='Abbaye de Belloc']"));
    }

    @Test(enabled = false)
    public void alertTest() {
        ((AndroidDriver) DriverManager.getDriver())
            .startActivity(new Activity("io.appium.android.apis", ".app.AlertDialogSamples"));

        page.tap(By.id("io.appium.android.apis:id/two_buttons"))

            .tap(By.id("android:id/button1"));
    }

    @Test(enabled = false)
    public void searchTest() {
        ((AndroidDriver) DriverManager.getDriver())
            .startActivity(new Activity("io.appium.android.apis", ".app.SearchInvoke"));
        page.enter(By.id("txt_query_prefill"), "Hello world!")
            .tap(By.id("btn_start_search"));
    }

    @AfterMethod
    void afterTest() {
        DriverManager.removeDriver();
        GridRunner.stopAppium();
    }
}
