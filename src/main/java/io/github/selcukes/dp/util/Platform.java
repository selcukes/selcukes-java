package io.github.selcukes.dp.util;

import io.github.selcukes.dp.enums.OSType;

public final class Platform {


    private Platform() {

    }

    public static int getPlatformArch() {

        return System.getProperty("os.arch").contains("64") ? 64 : 32;
    }

    public static OSType getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows"))
            return OSType.WIN;
        else if (osName.startsWith("mac") || osName.startsWith("darwin")) {
            return OSType.MAC;
        } else if (osName.contains("linux"))
            return OSType.LINUX;

        return null;
    }
}