/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.core.page;

import io.github.selcukes.commons.helper.FileHelper;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.print.PrintOptions;
import org.testng.Assert;

public class WebPage extends WebAuthenticator implements Page {
    WebDriver driver;

    public WebPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    public void printPage(String filePath) {
        PrintsPage pg = (PrintsPage) getDriver();
        PrintOptions printOptions = new PrintOptions();
        Pdf pdf = pg.print(printOptions);
        String pdfContent = pdf.getContent();
        Assert.assertTrue(pdfContent.contains("JVBER"));
        FileHelper.createFile(pdfContent, filePath);
    }

}
