/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.os;

import java.util.Objects;

public class Platform {

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private int arch;

    public static Platform getPlatform() {
        return new Platform();
    }

    public static boolean isWindows() {
        return (OS_NAME.contains("win"));
    }

    public static boolean isMac() {
        return (OS_NAME.contains("mac") || OS_NAME.startsWith("darwin"));
    }

    public static boolean isLinux() {
        return (OS_NAME.contains("nix") || OS_NAME.contains("nux"));
    }

    public OsType getOSType() {
        if (isWindows())
            return OsType.WIN;
        else if (isMac()) {
            return OsType.MAC;
        } else if (isLinux())
            return OsType.LINUX;

        return null;
    }

    public String getOsName() {
        return OS_NAME;
    }

    private int getPlatformArch() {

        return System.getProperty("os.arch").contains("64") ? 64 : 32;
    }

    public String getOsNameAndArch() {
        return (Objects.requireNonNull(getOSType()).name() + getArchitecture()).toLowerCase();
    }

    public int getArchitecture() {
        return arch != 0 ? arch : getPlatformArch();
    }

    public void setArchitecture(int archType) {
        arch = archType;
    }
}
