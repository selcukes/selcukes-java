/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.snapshot.tests;

import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.wdb.driver.LocalDriver;
import io.github.selcukes.wdb.enums.DriverType;
import lombok.CustomLog;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CustomLog
public class NativeSnapshotTest {
    private static final ThreadLocal<WebDriver> LOCAL_DRIVER = new InheritableThreadLocal<>();

    @DataProvider
    public Object[][] driverTypes() {
        return new Object[][]{{DriverType.CHROME}, {DriverType.EDGE}};
    }

    @Test(dataProvider = "driverTypes")
    public void browserTest(DriverType driverType) {
        logger.debug(() -> "DriverType : " + driverType);
        setDriver(driverType);
        new HomePage(getDriver()).navigateToHomePage();

    }

    @AfterMethod
    public void tearDown() {
        getDriver().quit();
    }

    @AfterClass
    void terminate() {
        LOCAL_DRIVER.remove();
    }

    private WebDriver getDriver() {
        return LOCAL_DRIVER.get();
    }

    private void setDriver(DriverType driverType) {
        LOCAL_DRIVER.set(new LocalDriver().createWebDriver(driverType, Platform.isLinux()));
    }
}
