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

import io.github.selcukes.core.page.Page;
import org.openqa.selenium.WebElement;

import static io.github.selcukes.core.validation.Validation.failWithMessage;

public class ElementValidation {
    Page page;
    WebElement element;
    boolean isSoft;

    public ElementValidation(boolean isSoft, Page page, WebElement element) {
        this.page = page;
        this.element = element;
        this.isSoft = isSoft;
    }

    public ElementValidation textAs(String expectedText) {
        String actual = element.getText();
        if (!actual.equalsIgnoreCase(expectedText)) {
            failWithMessage(isSoft, "Expected element [%s] should have text [%s] but was [%s]", element, expectedText, actual);
        }
        return this;
    }

    public ElementValidation containsText(String expectedText) {
        if (!element.getText().contains(expectedText)) {
            failWithMessage(isSoft, "Expected element [%s] to contain text [%s] but actual text was [%s]", element, expectedText, element.getText());
        }
        return this;
    }

    public ElementValidation notContainsText(String expectedText) {
        if (element.getText().contains(expectedText)) {
            failWithMessage(isSoft, "Expected element [%s] should not contains text [%s] but actual text was [%s]", expectedText, element.getText());
        }
        return this;
    }

    public ElementValidation valueAs(String expectedValue) {
        String actual = element.getAttribute("value");
        if (!actual.equalsIgnoreCase(expectedValue)) {
            failWithMessage(isSoft, "Expected element [%s] should have value [%s] but was [%s]", element, expectedValue, actual);
        }
        return this;
    }

    public ElementValidation attributeValueAs(String attribute, String expectedValue) {
        String actual = element.getAttribute(attribute);
        if (!actual.equalsIgnoreCase(expectedValue)) {
            failWithMessage(isSoft, "Expected element [%s] should have [%s] value [%s] but was [%s]", element, attribute, expectedValue, actual);
        }
        return this;
    }

    public ElementValidation isVisible() {
        if (!element.isDisplayed()) {
            failWithMessage(isSoft, "Expected element [%s] to be visible", element);
        }
        return this;
    }

    public ElementValidation isEnabled() {
        if (!element.isDisplayed()) {
            failWithMessage(isSoft, "Expected element [%s] to be enabled", element);
        }
        return this;
    }

    public ElementValidation isSelected() {
        if (!element.isSelected()) {
            failWithMessage(isSoft, "Expected element [%s] to be selected", element);
        }
        return this;
    }
}
