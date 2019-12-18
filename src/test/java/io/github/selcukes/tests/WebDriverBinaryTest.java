package io.github.selcukes.tests;


import io.github.selcukes.dp.DriverPool;
import org.junit.Assert;
import org.junit.Test;

import java.util.logging.Logger;


public class WebDriverBinaryTest {
    private Logger logger = Logger.getLogger(WebDriverBinaryTest.class.getName());

    @Test
    public void downloadBinaryTest() {
        String binProp = DriverPool.chromeDriver().targetPath("temp").setup();
        Assert.assertNotNull(binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + System.getProperty(binProp) + "}");
        binProp = DriverPool.ieDriver().targetPath("temp").arch32().setup();
        Assert.assertNotNull(binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + System.getProperty(binProp) + "}");
        binProp = DriverPool.firefoxDriver().targetPath("temp").version("v0.26.0").arch64().setup();
        Assert.assertNotNull(binProp);
        logger.info("Path set for the Property { " + binProp + "} from the location {" + System.getProperty(binProp) + "}");
    }
}
