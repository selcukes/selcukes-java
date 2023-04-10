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

package io.github.selcukes.core.tests;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.core.driver.DevicePool;
import io.github.selcukes.core.driver.DriverManager;
import io.github.selcukes.core.enums.DeviceType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DriverManagerTest {
    @Test
    public void testCustomOptions() {
        ConfigFactory.getConfig().getWeb().setRemote(false);
        DriverManager.createDriver(DeviceType.BROWSER, new ChromeOptions(), new EdgeOptions());
        assertTrue(DriverManager.getWrappedDriver() instanceof ChromeDriver);
        DriverManager.switchDriver(DeviceType.BROWSER, 1);
        assertTrue(DriverManager.getWrappedDriver() instanceof EdgeDriver);
    }

    @Test(enabled = false)
    public void devicePoolWithTwoDifferentOptionsTest() {
        DevicePool pool = new DevicePool();
        pool.addDevice(DeviceType.BROWSER, new ChromeOptions());
        pool.addDevice(DeviceType.BROWSER, new EdgeOptions());
        assertEquals(pool.getAllDevices().get(DeviceType.BROWSER).size(), 2);
    }

    @Test(enabled = false)
    public void devicePoolWithTwoSameOptionsTest() {
        DevicePool pool = new DevicePool();
        pool.addDevice(DeviceType.BROWSER, new ChromeOptions());
        pool.addDevice(DeviceType.BROWSER, new ChromeOptions());
        pool.addDevice(DeviceType.BROWSER, new ChromeOptions());
        assertEquals(pool.getAllDevices().get(DeviceType.BROWSER).size(), 1);
    }

}
