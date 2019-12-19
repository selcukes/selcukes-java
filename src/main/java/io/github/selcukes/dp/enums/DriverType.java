package io.github.selcukes.dp.enums;

public enum DriverType {
    CHROME("chrome"),
    FIREFOX("gecko"),
    IEXPLORER("ie"),
    EDGE("edge");
    String name;

    DriverType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
