package io.github.selcukes.wdb.util;

import io.github.selcukes.wdb.enums.OSType;

import java.util.Objects;

public final class Platform {

    private int archType;


    private int getPlatformArch() {

        return System.getProperty("os.arch").contains("64") ? 64 : 32;
    }

    public static Platform getPlatform() {
        return new Platform();
    }

    public OSType getOSType() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows"))
            return OSType.WIN;
        else if (osName.startsWith("mac") || osName.startsWith("darwin")) {
            return OSType.MAC;
        } else if (osName.contains("linux"))
            return OSType.LINUX;

        return null;
    }

    public int getArchitecture() {
        return archType != 0 ? archType : getPlatformArch();
    }

    public Platform setArchitecture(int archType) {
        this.archType = archType;
        return this;
    }

    public String getOsNameAndArch() {
        return (Objects.requireNonNull(getOSType()).name() + getArchitecture()).toLowerCase();
    }
}