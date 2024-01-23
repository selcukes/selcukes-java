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
import io.github.selcukes.collections.StringHelper;
import io.github.selcukes.commons.helper.Preconditions;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static java.lang.String.format;

@UtilityClass
public class Locator {
    static final String LOCATOR_SEPARATOR = ":";
    static final String INVALID_LOCATOR = "Invalid Locator[%s]";

    /**
     * If the locator is a string, parse it, otherwise return the locator
     *
     * @param  locator The locator to resolve.
     * @return         A By object
     */
    public static By resolve(Object locator) {
        return (locator instanceof String locatorString) ? parse(locatorString) : (By) locator;
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
        String locatorType = output[0].toLowerCase();
        String locatorValue = output[1];
        return switch (locatorType) {
            case "id" -> By.id(locatorValue);
            case "name" -> By.name(locatorValue);
            case "css", "cssselector" -> By.cssSelector(locatorValue);
            case "classname", "class" -> By.className(locatorValue);
            case "tagname", "tag" -> By.tagName(locatorValue);
            case "xpath" -> By.xpath(locatorValue);
            case "accessibilityid", "aid" -> AppiumBy.accessibilityId(locatorValue);
            case "linktext", "link" -> By.linkText(locatorValue);
            case "partiallinktext", "partiallink" -> By.partialLinkText(locatorValue);
            default -> throw new IllegalArgumentException("Unknown Locator type " + locatorType);
        };
    }
}
