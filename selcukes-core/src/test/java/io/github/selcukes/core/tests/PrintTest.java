package io.github.selcukes.core.tests;

import org.testng.annotations.Test;

public class PrintTest extends BaseTest {
    @Test
    public void testPrint() {
        page.open("https://techyworks.blogspot.com/2022/03/get-browser-session-storage-data-using-selenium.html");
        page.capturePageAsPdf("target/print.pdf");
    }
}
