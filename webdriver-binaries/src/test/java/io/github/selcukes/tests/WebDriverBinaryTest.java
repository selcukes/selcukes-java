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


import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.wdb.BinaryInfo;
import io.github.selcukes.wdb.WebDriverBinary;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static java.lang.System.getProperty;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class WebDriverBinaryTest {
    private final Logger logger = LoggerFactory.getLogger(WebDriverBinaryTest.class);

    @BeforeTest
    public void beforeTest() {
        ConfigFactory.loadLoggerProperties();
    }

    @Test
    public void chromeDriverTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().targetPath("temp").setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals("webdriver.chrome.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void firefoxDriverTest() {
        BinaryInfo binaryInfo = WebDriverBinary.firefoxDriver().targetPath("temp").version("v0.26.0").arch64().setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals("webdriver.gecko.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void ieDriverTest() {
        BinaryInfo binaryInfo = WebDriverBinary.ieDriver().targetPath("temp").arch32().setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals("webdriver.ie.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test(enabled = false)
    public void edgeDriverTest() {

        BinaryInfo binaryInfo = WebDriverBinary.edgeDriver().targetPath("temp").setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals("webdriver.edge.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void operaDriverTest() {

        BinaryInfo binaryInfo = WebDriverBinary.operaDriver().targetPath("temp").setup();
        String binProp = binaryInfo.getBinaryProperty();
        assertEquals("webdriver.opera.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void seleniumServerTest() {
        BinaryInfo binaryInfo = WebDriverBinary.grid().targetPath("temp").setup();
        String binaryPath = binaryInfo.getBinaryPath();
        File binaryFile = new File(binaryInfo.getBinaryPath());
        logger.debug(() -> "Binary path for selenium-server is {" + binaryPath + "}");
        assertTrue(binaryFile.exists(), "Binary file not exists in " + binaryPath);

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
    public void checkBrowserVersionTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().checkBrowserVersion().setup();
        String binProp = binaryInfo.getBinaryProperty();
        String binaryDownloadedPath = binaryInfo.getBinaryPath();
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath + "}");
    }

    @Test
    public void cleanBinaryCacheTest() {
        BinaryInfo binaryInfo = WebDriverBinary.chromeDriver().targetPath("temp").clearBinaryCache().setup();
        String binProp = binaryInfo.getBinaryProperty();
        String binaryDownloadedPath = binaryInfo.getBinaryPath();
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath + "}");
    }
}
