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

package io.github.selcukes.core.validation;

import io.github.selcukes.core.page.ui.Locator;
import lombok.CustomLog;
import org.openqa.selenium.WebElement;

import static io.github.selcukes.core.validation.Validation.failWithMessage;

@CustomLog
public class ElementValidation {

    private final WebElement element;
    private final String elementLocator;
    boolean isSoft;

    public ElementValidation(boolean isSoft, WebElement element) {

        this.element = element;
        this.isSoft = isSoft;
        this.elementLocator = Locator.of(element);
    }

    public ElementValidation textAs(String expectedText) {
        logger.info(() -> String.format("Verifying element [%s] should have text [%s]", elementLocator, expectedText));
        String actual = element.getText();
        if (!actual.equalsIgnoreCase(expectedText)) {
            failWithMessage(isSoft, "Expected element [%s] should have text [%s] but was [%s]", elementLocator, expectedText, actual);
        }
        return this;
    }

    public ElementValidation containsText(String expectedText) {
        logger.info(() -> String.format("Verifying element [%s] should contains text [%s]", elementLocator, expectedText));
        if (!element.getText().contains(expectedText)) {
            failWithMessage(isSoft, "Expected element [%s] to contain text [%s] but actual text was [%s]", elementLocator, expectedText, element.getText());
        }
        return this;
    }

    public ElementValidation notContainsText(String expectedText) {
        logger.info(() -> String.format("Verifying element [%s] should not contains text [%s]", elementLocator, expectedText));
        if (element.getText().contains(expectedText)) {
            failWithMessage(isSoft, "Expected element [%s] should not contains text [%s] but actual text was [%s]", expectedText, element.getText());
        }
        return this;
    }

    public ElementValidation valueAs(String expectedValue) {
        attributeValueAs("value", expectedValue);
        return this;
    }

    public ElementValidation attributeValueAs(String attribute, String expectedValue) {
        logger.info(() -> String.format("Verifying attribute [%s] value as [%s]", attribute, expectedValue));
        String actual = element.getAttribute(attribute);
        if (!actual.equalsIgnoreCase(expectedValue)) {
            failWithMessage(isSoft, "Expected element [%s] should have [%s] attribute with value [%s] but was [%s]", elementLocator, attribute, expectedValue, actual);
        }
        return this;
    }

    public ElementValidation isVisible() {
        logger.info(() -> String.format("Verifying element [%s] is visible", elementLocator));
        if (!element.isDisplayed()) {
            failWithMessage(isSoft, "Expected element [%s] to be visible", elementLocator);
        }
        return this;
    }

    public ElementValidation isEnabled() {
        logger.info(() -> String.format("Verifying element [%s] is enabled", elementLocator));
        if (!element.isDisplayed()) {
            failWithMessage(isSoft, "Expected element [%s] to be enabled", elementLocator);
        }
        return this;
    }

    public ElementValidation isSelected() {
        logger.info(() -> String.format("Verifying element [%s] is selected", elementLocator));
        if (!element.isSelected()) {
            failWithMessage(isSoft, "Expected element [%s] to be selected", elementLocator);
        }
        return this;
    }
}
