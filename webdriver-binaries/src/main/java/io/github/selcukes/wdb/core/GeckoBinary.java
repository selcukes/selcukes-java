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

import io.github.selcukes.commons.os.OsType;
import io.github.selcukes.commons.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.util.UrlHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class GeckoBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/geckodriver-%s-%s.%s";

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                UrlHelper.GECKODRIVER_URL,
                getBinaryVersion(),
                getBinaryVersion(),
                getBinaryEnvironment().getOsNameAndArch(),
                getCompressedBinaryType().getName()));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return Objects.equals(getBinaryEnvironment().getOSType(), OsType.WIN) ? DownloaderType.ZIP : DownloaderType.TAR;
    }

    @Override
    public String getBinaryDriverName() {
        return "geckodriver";
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.FIREFOX;
    }

    @Override
    protected String getLatestRelease() {
        return getVersionNumberFromGit(UrlHelper.GECKODRIVER_LATEST_RELEASE_URL);
    }
}