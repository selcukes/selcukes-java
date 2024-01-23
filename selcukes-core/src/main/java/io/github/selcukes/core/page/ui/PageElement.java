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

import io.github.selcukes.collections.StringHelper;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

import static java.util.Optional.ofNullable;

@UtilityClass
public final class PageElement {
    public static final String FILED_ATTRIBUTE_PROPERTY = "selcukes.page.fieldAttribute";
    public static final String IGNORE_FIELDS_PROPERTY = "selcukes.page.ignoreFields";
    private static final String DEFAULT_FIELD_ATTRIBUTE = "placeholder";
    private static final String FIELD_ATTRIBUTE;
    private static final List<String> SENSITIVE_FIELDS;

    static {
        String attribute = System.getProperty(FILED_ATTRIBUTE_PROPERTY);
        FIELD_ATTRIBUTE = StringHelper.isEmpty(attribute) ? DEFAULT_FIELD_ATTRIBUTE : attribute;
        String ignoreLabels = ofNullable(System.getProperty(IGNORE_FIELDS_PROPERTY))
                .orElse("password");
        SENSITIVE_FIELDS = List.of(ignoreLabels.split(","));
    }

    public String labelName(WebElement element) {
        if (element == null) {
            return "";
        }
        try {
            String text = element.getText();
            if (StringHelper.isNonEmpty(text)) {
                return text;
            }
            String attributeValue = element.getAttribute(FIELD_ATTRIBUTE);
            if (StringHelper.isNonEmpty(attributeValue)) {
                return attributeValue;
            }
        } catch (Exception ignored) {
            // Ignore
        }
        return Locator.of(element);
    }

    public String elementValue(String label, String value) {
        if (StringHelper.containsWord(label, SENSITIVE_FIELDS)) {
            return "*********";
        } else {
            return value;
        }
    }

    public String toLogMessage(WebElement element, CharSequence key) {
        if (key instanceof Keys keys) {
            return "Pressing Key " + keys.name();
        } else {
            String labelName = PageElement.labelName(element);
            String elementValue = PageElement.elementValue(labelName, key.toString());
            return String.format("Entering Text [%s] in %s Field", elementValue, labelName);
        }
    }
}
