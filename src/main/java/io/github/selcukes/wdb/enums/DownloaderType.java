package io.github.selcukes.wdb.enums;

public enum DownloaderType {
    ZIP("zip"),
    TAR("tar.gz"),
    UNKNOWN("");
    String name;

    DownloaderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
