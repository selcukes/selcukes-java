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
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.util.UrlHelper;
import io.github.selcukes.wdb.version.VersionComparator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static io.github.selcukes.wdb.util.OptionalUtil.unwrap;
import static org.jsoup.Jsoup.parse;

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
        final InputStream downloadStream = getHttpClient(UrlHelper.IEDRIVER_LATEST_RELEASE_URL).getResponseStream();
        List<String> versions = new ArrayList<>();
        Map<String, String> versionMap = new TreeMap<>();
        try {
            Document doc = parse(downloadStream, null, "");
            Elements element = doc.select(
                "Key:contains(" + matcher + ")");
            for (Element e : element) {
                String key = e.text().substring(e.text().indexOf('/'));
                versionMap.put(key, e.text());
                String temp = e.text().substring(e.text().indexOf('/') + 1).replaceAll(matcher, "");
                String versionNum = temp.substring(1, temp.length() - 4);
                versions.add(versionNum);
            }

            versions.sort(new VersionComparator());

            String version = versions.get(versions.size() - 1);
            latestVersionUrl = unwrap(versionMap.entrySet().stream()
                .filter(map -> map.getValue().contains(version)).findFirst()).getValue();

            return version;

        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        }
    }

}