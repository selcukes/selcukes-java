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

package io.github.selcukes.tests;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.wdb.BinaryInfo;
import io.github.selcukes.wdb.WebDriverBinary;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

import static java.lang.System.getProperty;
import static org.testng.Assert.assertEquals;

public class WebDriverBinaryTest {
    private final Logger logger = LoggerFactory.getLogger(WebDriverBinaryTest.class);

    @Test
    public void chromeDriverTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals(binProp, "webdriver.chrome.driver");
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
        var options = new ChromeOptions();
        options.addArguments("--headless=new");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.google.com/");
        driver.quit();
    }

    @Test
    public void firefoxDriverTest() {
        BinaryInfo binaryInfo = WebDriverBinary.firefoxDriver().version("v0.26.0").arch64().setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals(binProp, "webdriver.gecko.driver");
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void ieDriverTest() {
        BinaryInfo binaryInfo = WebDriverBinary.ieDriver().arch32().setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals(binProp, "webdriver.ie.driver");
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test(enabled = false)
    public void edgeDriverTest() {

        BinaryInfo binaryInfo = WebDriverBinary.edgeDriver().setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals(binProp, "webdriver.edge.driver");
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void operaDriverTest() {

        BinaryInfo binaryInfo = WebDriverBinary.operaDriver().setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals(binProp, "webdriver.opera.driver");
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void reuseDownloadedBinaryTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().setup();
        String binProp = binaryInfo.getBinaryProperty();
        String binaryDownloadedPath = binaryInfo.getBinaryPath();
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath + "}");

        BinaryInfo binaryInfo1 = WebDriverBinary.chromeDriver().setup();
        String binaryDownloadedPath1 = binaryInfo1.getBinaryPath();
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath1 + "}");
        assertEquals(binaryDownloadedPath, binaryDownloadedPath1);

    }

    @Test
    public void strictDownloadTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().strictDownload().setup();
        String binProp = binaryInfo.getBinaryProperty();
        String binaryDownloadedPath = binaryInfo.getBinaryPath();
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath + "}");
    }

    @Test
    public void disableAutoBrowserVersionCheckTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().disableAutoCheck().setup();
        String binProp = binaryInfo.getBinaryProperty();
        String binaryDownloadedPath = binaryInfo.getBinaryPath();
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath + "}");
    }

    @Test(priority = 1000)
    public void cleanBinaryCacheTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().clearBinaryCache().setup();
        String binProp = binaryInfo.getBinaryProperty();
        String binaryDownloadedPath = binaryInfo.getBinaryPath();
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath + "}");
    }
}
