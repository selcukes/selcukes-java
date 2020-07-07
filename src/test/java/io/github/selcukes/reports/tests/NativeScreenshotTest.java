/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package io.github.selcukes.reports.tests;

import io.github.selcukes.reports.image.Snapshot;
import io.github.selcukes.reports.image.SnapshotImpl;
import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class NativeScreenshotTest {

    @Test
    public void nativeScreenshotTest() {
        WebDriverBinary.chromeDriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://techyworks.blogspot.com/");
        Snapshot snapshot = new SnapshotImpl(driver);
        System.out.println(snapshot.shootFullPage());

    }
}
