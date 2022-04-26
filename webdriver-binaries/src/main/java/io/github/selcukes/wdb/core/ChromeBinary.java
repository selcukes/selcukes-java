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
import io.github.selcukes.commons.os.Architecture;
import io.github.selcukes.commons.os.OsType;
import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.util.BinaryDownloader;
import io.github.selcukes.wdb.util.UrlHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static io.github.selcukes.wdb.util.OptionalUtil.unwrap;

public class ChromeBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/chromedriver_%s.zip";

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                UrlHelper.CHROMEDRIVER_URL,
                getBinaryVersion(),
                getBinaryEnvironment().getOsNameAndArch()));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public Platform getBinaryEnvironment() {
        Platform platform = Platform.getPlatform();
        if (getTargetArch().isPresent())
            platform.setArchitecture(unwrap(getTargetArch()).getValue());
        else if (Objects.equals(platform.getOSType(), OsType.WIN))
            platform.setArchitecture(Architecture.X32.getValue());
        return platform;
    }

    @Override
    public String getBinaryDriverName() {
        return "chromedriver";
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.CHROME;
    }

    @Override
    public void browserVersion(boolean isAutoCheck) {
        if (isAutoCheck) {
            setBrowserVersion(UrlHelper.CHROMEDRIVER_URL);
        }
    }

    @Override
    protected String getLatestRelease() {
        try {
            return BinaryDownloader.downloadAndReadFile(new URL(UrlHelper.CHROMEDRIVER_LATEST_RELEASE_URL));
        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }
}