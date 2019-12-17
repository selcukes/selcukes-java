package io.github.selcukes.tests;

import io.github.selcukes.dp.DriverPool;

public class WebDriverBinaryTest {
    public static void main(String[] args) {
        DriverPool.chromeDriver().setup();
        DriverPool.ieDriver().targetPath("temp").arch32().setup();
        DriverPool.firefoxDriver().targetPath("temp").version("v0.26.0").arch64().setup();
    }
}
