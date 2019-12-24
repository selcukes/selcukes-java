package io.github.selcukes.wdb.enums;

public enum DriverType {
    CHROME("chrome"),
    FIREFOX("gecko"),
    IEXPLORER("ie"),
    EDGE("edge"),
    GRID("selenium-server");

    String name;

    DriverType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
