package com.techyworks.tests;

import io.github.selcukes.dp.DriverPool;

public class WebDriverBinaryTest {
    public static void main(String[] args) {

        DriverPool.firefoxDriver().arch64().targetPath("temp").version("v0.26.0").setup();
     

    }
}
