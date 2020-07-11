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

import io.github.selcukes.commons.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.util.UrlHelper;

import java.net.MalformedURLException;
import java.net.URL;

public class IExplorerBinary extends AbstractBinary {

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(UrlHelper.IEDRIVER_URL + "/" + latestVersionUrl);

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public String getBinaryDriverName() {
        return "IEDriverServer";
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.IEXPLORER;
    }

    @Override
    protected String getLatestRelease() {
        String arch = getBinaryEnvironment().getArchitecture() == 64 ? "x64" : "Win32";
        String matcher = "IEDriverServer" + "_" + arch;
        return getVersionNumberFromXML(UrlHelper.IEDRIVER_LATEST_RELEASE_URL, matcher);
    }

}