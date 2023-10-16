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

import io.appium.java_client.android.AndroidDriver;
import io.github.selcukes.commons.annotation.Lifecycle;
import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.page.MobilePage;
import io.github.selcukes.core.page.Pages;
import io.github.selcukes.core.wait.WaitCondition;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static io.github.selcukes.core.enums.SwipeDirection.DOWN;
import static io.github.selcukes.core.enums.SwipeDirection.UP;
import static java.lang.String.format;

@Lifecycle
public class MobileAppTest {
    MobilePage page;

    @BeforeMethod
    void beforeTest() {
        page = Pages.mobilePage();
    }

    private By textView(String text) {
        return By.xpath(format("//android.widget.TextView[@text='%s']", text));
    }

    @Test(enabled = false)
    public void expandAndScrollScreenTest() {
        page.click("aid:Views")
                .click("aid:Expandable Lists")
                .click("aid:3. Simple Adapter")
                .swipe(textView("Group 18"), DOWN)
                .click(textView("Group 18"))
                .swipe(textView("Child 13"), DOWN)
                .swipe(textView("Group 1"), UP);

    }

    @Test(enabled = false)
    public void expandAndScrollElementTest() {
        String splittingTouches = "aid:Splitting Touches across Views";
        String listTextView = "//android.widget.ListView[2]/android.widget.TextView[@text='%s']";
        By blueText = By.xpath(format(listTextView, "Blue"));
        By sublimeText = By.xpath(format(listTextView, "Abbaye de Belloc"));
        By list2 = By.id("io.appium.android.apis:id/list2");

        page.click("aid:Views")
                .swipe(splittingTouches, DOWN)
                .click(splittingTouches)
                .swipe(list2, blueText, DOWN)
                .click(blueText, WaitCondition.PRESENT)
                .swipe(list2, sublimeText, UP)
                .click(sublimeText);
    }

    @Test(enabled = false)
    public void alertTest() {
        ((AndroidDriver) DriverManager.getWrappedDriver()).executeScript("mobile: startActivity",
            Map.of(
                "component", String.format("%s/%s", "io.appium.android.apis", ".app.AlertDialogSamples")));

        page.click(By.id("io.appium.android.apis:id/two_buttons"))
                .click(By.id("android:id/button1"));
    }

    @Test(enabled = false)
    public void dragAndDrop() {
        By from = By.id("io.appium.android.apis:id/drag_dot_1");
        By to = By.id("io.appium.android.apis:id/drag_dot_2");
        By dragText = By.id("io.appium.android.apis:id/drag_result_text");
        page.click("aid:Views")
                .click("aid:Drag and Drop")
                .dragAndDrop(from, to)
                .assertThat().element(dragText).textAs("DraggableDot");
    }

    @Test(enabled = false)
    public void searchTest() {
        ((AndroidDriver) DriverManager.getWrappedDriver()).executeScript("mobile: startActivity",
            Map.of(
                "component", String.format("%s/%s", "io.appium.android.apis", ".app.SearchInvoke")));
        page.enter(By.id("txt_query_prefill"), "Hello world!")
                .click(By.id("btn_start_search"));
    }

}
