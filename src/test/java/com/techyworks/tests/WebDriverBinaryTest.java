package com.techyworks.tests;

import io.github.selcukes.dp.DriverPool;

public class WebDriverBinaryTest {
    public static void main(String[] args) {

        DriverPool.chromeDriver().targetPath("temp").arch64().setup();

    }
}
