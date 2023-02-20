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

package io.github.selcukes.core.page.ui;

import io.appium.java_client.AppiumBy;
import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.databind.utils.StringHelper;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static java.lang.String.format;

@UtilityClass
public class Locator {
    public static final String LOCATOR_SEPARATOR = ":";
    public static final String INVALID_LOCATOR = "Invalid Locator[%s]";

    /**
     * If the locator is a string, parse it, otherwise return the locator
     *
     * @param  locator The locator to resolve.
     * @return         A By object
     */
    public static By resolve(Object locator) {
        return (locator instanceof String) ? parse((String) locator) : (By) locator;
    }

    /**
     * It takes a WebElement as an argument and returns the locator of the
     * element
     *
     * @param  element The element to be converted to a string.
     * @return         The locator of the element.
     */
    public static String of(WebElement element) {
        return StringHelper.findPattern("->\\s(.*)(?=])", element.toString())
                .orElse(element.getText());
    }

    private By parse(String locator) {
        Preconditions.checkArgument(locator.contains(LOCATOR_SEPARATOR), format(INVALID_LOCATOR, locator));
        String[] output = locator.split(LOCATOR_SEPARATOR);
        String locatorType = output[0];
        String locatorValue = output[1];
        switch (locatorType.toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "css":
            case "cssselector":
                return By.cssSelector(locatorValue);
            case "classname":
            case "class":
                return By.className(locatorValue);
            case "tagname":
            case "tag":
                return By.tagName(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "accessibilityid":
            case "aid":
                return AppiumBy.accessibilityId(locatorValue);
            case "linktext":
            case "link":
                return By.linkText(locatorValue);
            case "partiallinktext":
            case "partiallink":
                return By.partialLinkText(locatorValue);
            default:
                throw new SelcukesException("Unknown Locator type " + locatorType);
        }
    }
}
