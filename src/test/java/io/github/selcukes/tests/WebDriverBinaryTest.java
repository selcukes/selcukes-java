package io.github.selcukes.tests;


import io.github.selcukes.dp.WebDriverBinaries;
import org.junit.Test;

import java.util.logging.Logger;

import static java.lang.System.getProperty;
import static org.junit.Assert.assertEquals;


public class WebDriverBinaryTest {
    private Logger logger = Logger.getLogger(WebDriverBinaryTest.class.getName());

    @Test
    public void downloadBinaryTest() {
        String binProp = WebDriverBinaries.chromeDriver().targetPath("temp").setup();
        assertEquals("webdrivers.chrome.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = WebDriverBinaries.ieDriver().targetPath("temp").arch32().setup();
        assertEquals("webdrivers.ie.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = WebDriverBinaries.firefoxDriver().targetPath("temp").version("v0.26.0").arch64().setup();
        assertEquals("webdrivers.gecko.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");

        binProp = WebDriverBinaries.edgeDriver().targetPath("temp").setup();
        assertEquals("webdrivers.edge.driver", binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + getProperty(binProp) + "}");
    }

    @Test
    public void reuseDownloadedBinaryTest() {
        String binProp = WebDriverBinaries.chromeDriver().setup();
        String binaryDownloadedPath = getProperty(binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + binaryDownloadedPath + "}");

        String binProp1 = WebDriverBinaries.chromeDriver().setup();
        String binaryDownloadedPath1 = getProperty(binProp1);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + binaryDownloadedPath1 + "}");

        assertEquals(binaryDownloadedPath, binaryDownloadedPath1);

    }
}
