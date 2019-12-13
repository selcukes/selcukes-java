package io.github.selcukes.dp.core;

import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.util.OSUtil;

public class Environment {
    private int archType;

    private Environment() {
    }

    private Environment(int archType) {
        this.archType = archType;
    }

    public static Environment create() {
        return new Environment();
    }

    public static Environment create(int archType) {
        return new Environment(archType);
    }

    public OSType getOSType() {
        return OSUtil.getOSType();
    }

    public int getArchitecture() {
        return archType != 0 ? archType : OSUtil.getOsArch();
    }

    public String getOsNameAndArch() {
        return (getOSType().name() + getArchitecture()).toLowerCase();
    }
}