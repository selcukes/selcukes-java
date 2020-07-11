package io.github.selcukes.core.script;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JavaScriptImpl implements JavaScript {
    JavascriptExecutor js;
    Object result;

    public JavaScriptImpl(WebDriver driver) {
        js = (JavascriptExecutor) driver;
    }

    @Override
    public JavaScriptImpl execute(String script, Object... args) {
        result = js.executeScript(script, args);
        return this;
    }

    public Object getResult() {
        return result;
    }

}
