package io.github.selcukes.tests;


import io.github.selcukes.wdb.WebDriverBinary;


import java.util.logging.Logger;

import org.testng.annotations.Test;

import static java.lang.System.getProperty;

import static org.testng.Assert.assertEquals;


public class WebDriverBinaryTest {
    private Logger logger = Logger.getLogger(WebDriverBinaryTest.class.getName());

    @Test
    public void downloadBinaryTest() {
        String binProp = WebDriverBinary.chromeDriver().targetPath("temp").setup();
        assertEquals("webdriver.chrome.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = WebDriverBinary.ieDriver().targetPath("temp").arch32().setup();
        assertEquals("webdriver.ie.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = WebDriverBinary.firefoxDriver().targetPath("temp").version("v0.26.0").arch64().setup();
        assertEquals("webdriver.gecko.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = WebDriverBinary.edgeDriver().targetPath("temp").setup();
        assertEquals("webdriver.edge.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = WebDriverBinary.grid().targetPath("temp").setup();
        assertEquals("webdriver.grid.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");
    }

    @Test
    public void reuseDownloadedBinaryTest() {
        String binProp = WebDriverBinary.chromeDriver().setup();
        String binaryDownloadedPath = getProperty(binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + binaryDownloadedPath + "}");

        String binProp1 = WebDriverBinary.chromeDriver().setup();
        String binaryDownloadedPath1 = getProperty(binProp1);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + binaryDownloadedPath1 + "}");

        assertEquals(binaryDownloadedPath, binaryDownloadedPath1);

    }
}
