/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.element;

import io.github.selcukes.commons.Await;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

public class Element implements WrapsElement {
    WebElement element;

    public Element(WebElement element) {
        this.element = element;
    }

    public WebElement getWrappedElement() {
        return getWrappedElement(element);
    }

    public WebElement getElement() {
        return element;
    }

    public WebElement getWrappedElement(WebElement element) {
        return ((WrapsElement) element).getWrappedElement();
    }

    public String tagName() {
        return element.getTagName();
    }

    public Element click() {
        element.click();
        return this;
    }


    public Element doubleClick() {

        return this;
    }


    public Element contextClick() {

        return this;
    }

    public Element waitAndClick() {
        return waitAndClick(5);
    }


    public Element waitAndClick(int duration) {
        Await.until(duration);
        this.click();
        return this;
    }

}
