package io.github.selcukes.dp.util;

import io.github.selcukes.dp.enums.OSType;

import java.util.function.BiPredicate;
import java.util.function.Function;

public final class OSUtil {
    private OSUtil() {

    }

    private static BiPredicate<String, String> osNameTest = (osName, criteria) -> osName.toLowerCase().contains(criteria);
    private static Function<String, Integer> osArchFunc = osArch -> (osArch.contains("86") && !osArch.contains("64")) ? 32 : 64;

    public static OSType getOSType() {
        final String osName = System.getProperty("os.name");
        return osNameTest.test(osName, "windows") ? OSType.WIN : osNameTest.test(osName, "mac") ? OSType.MAC : OSType.LINUX;
    }

    public static int getOsArch() {
        final String osArch = System.getProperty("os.arch");
        return osArchFunc.apply(osArch.toLowerCase());
    }
}