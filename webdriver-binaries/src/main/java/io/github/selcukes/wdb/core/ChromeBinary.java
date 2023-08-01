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
import io.github.selcukes.wdb.util.UrlHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class ChromeBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/%s/chromedriver-%s.zip";

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    UrlHelper.CHROMEDRIVER_URL,
                    getBinaryVersion(),
                    getBinaryEnvironment().getOsNameAndArch(),
                    getBinaryEnvironment().getOsNameAndArch()));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public Platform getBinaryEnvironment() {
        var platform = Platform.getPlatform();
        var arch = getTargetArch();
        if (arch.isPresent()) {
            platform.setArchitecture(arch.get().getValue());
        } else if (Objects.equals(platform.getOSType(), OsType.WIN)) {
            platform.setArchitecture(Architecture.X32.getValue());
        }
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
        return getVersionNumberFromXml(UrlHelper.CHROMEDRIVER_LATEST_RELEASE_URL);
    }
}
