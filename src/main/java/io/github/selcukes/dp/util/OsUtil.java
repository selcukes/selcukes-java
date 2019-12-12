package io.github.selcukes.dp.util;

import io.github.selcukes.dp.enums.OsType;

import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * The type Os util.
 */
public final class OsUtil {

    private static BiPredicate<String, String> osNameTest = (osName, criteria) -> osName.toLowerCase().contains(criteria);
    private static Function<String, Integer> osArchFunc = (osArch) -> (osArch.contains("86") && !osArch.contains("64")) ? 32 : 64;

    /**
     * Gets os type.
     *
     * @return the os type
     */
    public static OsType getOsType() {
        final String osName = System.getProperty("os.name");
        return osNameTest.test(osName, "windows") ? OsType.WIN : osNameTest.test(osName, "mac") ? OsType.MAC : OsType.LINUX;
    }

    /**
     * Gets os arch.
     *
     * @return the os arch
     */
    public static int getOsArch() {
        final String osArch = System.getProperty("os.arch");
        return osArchFunc.apply(osArch.toLowerCase());
    }
}