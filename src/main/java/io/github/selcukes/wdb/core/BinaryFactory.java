/*
 *
 *  * Copyright (c) Ramesh Babu Prudhvi.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.selcukes.wdb.core;

import io.github.selcukes.core.commons.os.Architecture;
import io.github.selcukes.core.commons.os.OsType;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.util.Platform;
import io.github.selcukes.wdb.util.FileUtil;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public interface BinaryFactory {
    URL getDownloadURL();

    Platform getBinaryEnvironment();

    default File getCompressedBinaryFile() {
        String file = FileUtil.getTempDirectory() +
            "/" +
            getBinaryDriverName().toLowerCase() +
            "_" +
            getBinaryVersion() +
            "." + getCompressedBinaryType().getName();
        return new File(file);
    }

    default DownloaderType getCompressedBinaryType() {
        return DownloaderType.ZIP;
    }

    default String getBinaryFileName() {
        return Objects.equals(getBinaryEnvironment().getOSType(), OsType.WIN) ? getBinaryDriverName() + ".exe" : getBinaryDriverName();
    }

    default String getBinaryDirectory() {
        return getBinaryDriverName().toLowerCase() + "_" + getBinaryVersion();
    }

    String getBinaryDriverName();

    String getBinaryVersion();

    DriverType getDriverType();

    void setVersion(String version);

    void setTargetArch(Architecture targetArch);

    void setProxy(String proxy);

    void browserVersion(boolean isAutoCheck);
}