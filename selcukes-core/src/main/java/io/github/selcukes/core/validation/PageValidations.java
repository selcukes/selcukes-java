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

import io.appium.java_client.AppiumBy;
import io.github.selcukes.commons.http.WebResponse;
import io.github.selcukes.core.page.Page;
import lombok.CustomLog;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static io.github.selcukes.core.validation.Validation.failWithMessage;

@CustomLog
public class PageValidations {
    Page page;
    boolean isSoft;

    public PageValidations(Page page, boolean isSoft) {
        this.page = page;
        this.isSoft = isSoft;
    }

    public void title(String expectedTitle) {
        logger.info(() -> String.format("Verifying Page title should be [%s]", expectedTitle));
        if (!page.title().equalsIgnoreCase(expectedTitle)) {
            failWithMessage(isSoft, "Expected Page title should be [%s] but was [%s]", expectedTitle, page.title());
        }
    }

    public void titleContains(String expectedTitle) {
        logger.info(() -> String.format("Verifying Page title should contains text [%s]", expectedTitle));
        if (!page.title().contains(expectedTitle)) {
            failWithMessage(isSoft, "Expected Page Title should contains text [%s] but was [%s]", expectedTitle,
                page.title());
        }
    }

    public void url(String url) {
        logger.info(() -> String.format("Verifying Page URL should be [%s]", url));
        if (!page.currentUrl().equalsIgnoreCase(url)) {
            failWithMessage(isSoft, "Expected Page URL should be [%s] but was [%s]", url, page.currentUrl());
        }
    }

    public void urlContains(String expectedValue) {
        logger.info(() -> String.format("Verifying Page URL should contains [%s]", expectedValue));
        if (!page.currentUrl().contains(expectedValue)) {
            failWithMessage(isSoft, "Expected Page URL should contains [%s] but was [%s]", expectedValue,
                page.currentUrl());
        }
    }

    public ElementValidation element(String accessibilityId) {
        return new ElementValidation(isSoft, page.find(AppiumBy.accessibilityId(accessibilityId)));
    }

    public ElementValidation element(By by) {
        return new ElementValidation(isSoft, page.find(by));
    }

    public ElementValidation element(WebElement element) {
        return new ElementValidation(isSoft, element);
    }

    public ResponseValidation response(WebResponse response) {
        return new ResponseValidation(isSoft, response);
    }

    public ObjectValidation object(Object object) {
        return new ObjectValidation(isSoft, object);
    }

}
