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

package io.github.selcukes.wdb.core;

import io.github.selcukes.commons.exception.WebDriverBinaryException;
import io.github.selcukes.commons.os.OsType;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.util.BinaryDownloader;
import io.github.selcukes.wdb.util.UrlHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class EdgeBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/edgedriver_%s.zip";

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                UrlHelper.EDGE_DRIVER_URL,
                getBinaryVersion(),
                Objects.equals(getBinaryEnvironment().getOSType(), OsType.LINUX) ? "win" + getBinaryEnvironment().getArchitecture() : getBinaryEnvironment().getOsNameAndArch()
            ));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public String getBinaryDriverName() {
        return "msedgedriver";
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.EDGE;
    }

    @Override
    protected String getLatestRelease() {
        try {
            String latestVersion = BinaryDownloader.downloadAndReadFile(new URL(UrlHelper.EDGE_DRIVER_LATEST_RELEASE_URL));
            return latestVersion.replaceAll("[^A-Za-z0-9_\\\\.]", "");
        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

}