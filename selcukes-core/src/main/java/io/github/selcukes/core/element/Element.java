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
