package io.github.selcukes.tests;


import io.github.selcukes.logging.Logger;
import io.github.selcukes.logging.LoggerFactory;
import io.github.selcukes.wdb.WebDriverBinary;
import org.testng.annotations.Test;

import static java.lang.System.getProperty;
import static org.testng.Assert.assertEquals;


public class WebDriverBinaryTest {
    private Logger logger = LoggerFactory.getLogger(WebDriverBinaryTest.class);

    @Test
    public void chromeDriverTest() {
        String binProp = WebDriverBinary.chromeDriver().targetPath("temp").setup();
        assertEquals("webdriver.chrome.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void firefoxDriverTest() {
        String binProp = WebDriverBinary.firefoxDriver().targetPath("temp").version("v0.26.0").arch64().setup();
        assertEquals("webdriver.gecko.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void ieDriverTest() {
        String binProp = WebDriverBinary.ieDriver().targetPath("temp").arch32().setup();
        assertEquals("webdriver.ie.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void edgeDriverTest() {

        String binProp = WebDriverBinary.edgeDriver().targetPath("temp").setup();
        assertEquals("webdriver.edge.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void seleniumServerTest() {
        String binProp = WebDriverBinary.grid().targetPath("temp").setup();
        assertEquals("webdriver.grid.driver", binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + getProperty(binProp) + "}");
    }

    @Test
    public void reuseDownloadedBinaryTest() {
        String binProp = WebDriverBinary.chromeDriver().setup();
        String binaryDownloadedPath = getProperty(binProp);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath + "}");

        String binProp1 = WebDriverBinary.chromeDriver().setup();
        String binaryDownloadedPath1 = getProperty(binProp1);
        logger.debug(() -> "Binary path for { " + binProp + "} is {" + binaryDownloadedPath1 + "}");
        assertEquals(binaryDownloadedPath, binaryDownloadedPath1);

    }
}
