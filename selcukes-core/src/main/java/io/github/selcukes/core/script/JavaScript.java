package io.github.selcukes.core.script;

public interface JavaScript {
    JavaScriptImpl execute(String script, Object... args);
}
