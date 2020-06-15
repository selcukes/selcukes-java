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

import io.github.selcukes.core.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.enums.OSType;
import io.github.selcukes.wdb.util.UrlHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.jsoup.Jsoup.parse;

public class EdgeBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/edgedriver_%s.zip";

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                UrlHelper.EDGE_DRIVER_URL,
                getBinaryVersion(),
                Objects.equals(getBinaryEnvironment().getOSType(), OSType.LINUX) ? "win" + getBinaryEnvironment().getArchitecture() : getBinaryEnvironment().getOsNameAndArch()
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
        List<String> versionNumbers = new ArrayList<>();
        String latestVersion;
        final InputStream downloadStream = getHttpClient(UrlHelper.EDGE_DRIVER_LATEST_RELEASE_URL).getResponseStream();
        try {
            Document doc = parse(downloadStream, null, "");

            Elements versionParagraph = doc.select(
                "ul.driver-downloads li.driver-download p.driver-download__meta");

            for (Element element : versionParagraph) {
                if (element.text().toLowerCase().startsWith("version")) {
                    String[] version = element.text().split(" ");
                    versionNumbers.add(version[1]);
                }
            }

            latestVersion = versionNumbers.get(0);

        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        }
        return latestVersion;
    }
}