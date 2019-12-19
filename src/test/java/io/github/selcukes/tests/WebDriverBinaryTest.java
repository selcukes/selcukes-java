package io.github.selcukes.tests;


import io.github.selcukes.dp.DriverPool;
import org.junit.Test;

import java.util.logging.Logger;

import static java.lang.System.getProperty;
import static org.junit.Assert.assertEquals;


public class WebDriverBinaryTest {
    private Logger logger = Logger.getLogger(WebDriverBinaryTest.class.getName());

    @Test
    public void downloadBinaryTest() {
        String binProp = DriverPool.chromeDriver().targetPath("temp").setup();
        assertEquals("webdrivers.chrome.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = DriverPool.ieDriver().targetPath("temp").arch32().setup();
        assertEquals("webdrivers.ie.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = DriverPool.firefoxDriver().targetPath("temp").version("v0.26.0").arch64().setup();
        assertEquals("webdrivers.gecko.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = DriverPool.edgeDriver().version("81.0.368.0").targetPath("temp").setup();
        String binaryDownloadedPath = getProperty(binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");
    }

    @Test
    public void reuseDownloadedBinaryTest() {
        String binProp = DriverPool.chromeDriver().setup();
        String binaryDownloadedPath = getProperty(binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + binaryDownloadedPath + "}");

        String binProp1 = DriverPool.chromeDriver().setup();
        String binaryDownloadedPath1 = getProperty(binProp1);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + binaryDownloadedPath1 + "}");

        assertEquals(binaryDownloadedPath, binaryDownloadedPath1);

    }
}
