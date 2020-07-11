package io.github.selcukes.core.script;

public interface JavaScriptControl {
    JavaScriptImpl execute(String script, Object... args);
}
