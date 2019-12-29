package io.github.selcukes.wdb.enums;

public enum DriverType {
    CHROME("chrome"),
    FIREFOX("gecko"),
    IEXPLORER("ie"),
    EDGE("edge"),
    OPERA("opera"),
    GRID("grid");

    String name;

    DriverType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
