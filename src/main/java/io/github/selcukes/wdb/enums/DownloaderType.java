package io.github.selcukes.wdb.enums;

public enum DownloaderType {
    ZIP("zip"),
    TAR("tar.gz");
    String name;

    DownloaderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
