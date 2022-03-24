package io.github.selcukes.core.tests;

import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.print.PrintOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class PrintTest extends BaseTest{
    @Test
    public void testPrint() throws IOException {
        driver.get("https://techyworks.blogspot.com/2022/03/get-browser-session-storage-data-using-selenium.html");
        PrintsPage pg = (PrintsPage) driver;
        PrintOptions printOptions = new PrintOptions();
        Pdf pdf = pg.print(printOptions);

        String pdfBase64 = pdf.getContent();
        Assert.assertTrue(pdfBase64.contains("JVBER"));

        byte[] decodedImg = Base64.getDecoder()
            .decode(pdfBase64.getBytes(StandardCharsets.UTF_8));
        Path destinationFile = Paths.get("my-pdf.pdf");
        Files.write(destinationFile, decodedImg);
    }
}
