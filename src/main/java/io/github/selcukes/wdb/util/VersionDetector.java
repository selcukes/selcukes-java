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
package io.github.selcukes.wdb.util;

import io.github.selcukes.core.commons.exec.ExecResults;
import io.github.selcukes.core.commons.exec.Shell;
import io.github.selcukes.core.commons.os.Platform;
import io.github.selcukes.core.exception.WebDriverBinaryException;
import io.github.selcukes.core.http.HttpClient;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jsoup.Jsoup.parse;

public class VersionDetector {
    Logger logger = LoggerFactory.getLogger(getClass());
    String driverName;
    String osNameAndArch;
    String binaryDownloadUrl;

    public VersionDetector(String binaryDriverName, String osNameAndArch, String binaryDownloadUrl) {
        this.driverName = binaryDriverName;
        this.osNameAndArch = osNameAndArch;
        this.binaryDownloadUrl = binaryDownloadUrl;
    }

    public String getVersion() {
        Optional<String> regQuery = Optional.empty();

        if (Platform.isWindows()) {
            regQuery = Optional.ofNullable(getRegQuery());
        }
        else if (Platform.isLinux())
        {

            Shell shell = new Shell();
            ExecResults execResults = shell.runCommand("which google-chrome-stable");
            String chromePath=execResults.getOutput().toString();
            System.out.println(chromePath);
            String command="google-chrome --version | grep -iE \"[0-9.]{10,20}\"";
            System.out.println(shell.runCommand(command).getOutput());
        }

        return regQuery.map(this::getBrowserVersionFromRegistry).orElse(null);
    }

    private String getRegQuery() {
        String regQuery = "reg query \"%s\" /v version";
        String chKey = "HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon";
        String ffKey = "HKLM\\Software\\Mozilla\\Mozilla Firefox";
        String ieKey = "HKLM\\Software\\Microsoft\\Internet Explorer";
        String key = null;
        if (driverName.contains("chrome"))
            key = chKey;
        else if (driverName.contains("gecko"))
            key = ffKey;
        else if (driverName.contains("IE"))
            key = ieKey;
        else return null;
        return String.format(regQuery, key);
    }

    private String getWMICQuery() {
        String wmicQuery = "wmic datafile where name='%s' get version";
        String ffKey = "C:\\\\program files (x86)\\\\Mozilla Firefox\\\\firefox.exe";
        String chKey = "C:\\\\Program Files (x86)\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe";
        String ieKey = "C:\\\\Program Files\\\\Internet Explorer\\\\iexplore.exe";
        String key = null;
        if (driverName.contains("chrome"))
            key = chKey;
        else if (driverName.contains("gecko"))
            key = ffKey;
        else if (driverName.contains("IE"))
            key = ieKey;
        else return null;
        return String.format(wmicQuery, key);
    }

    private String getBrowserVersionFromRegistry(String regQuery) {
        Shell shell = new Shell();
        ExecResults execResults = shell.runCommand(regQuery);

        String[] words = execResults.getOutput().get(2).split(" ");
        String browserVersion = words[words.length - 1];

        logger.info(() -> "Browser Version Number: " + browserVersion);

        return getCompatibleBinaryVersion(browserVersion);
    }

    public List<String> getVersionNumbers(String binaryDownloadUrl, String matcher) {
        HttpClient client = new HttpClient(binaryDownloadUrl, null);
        final InputStream downloadStream = client.getResponseStream();
        List<String> versions = new ArrayList<>();

        try {
            Document doc = parse(downloadStream, null, "");
            Elements element = doc.select(
                "Key:contains(" + matcher + ")");
            for (Element e : element) {
                String versionNum = e.text().substring(0, e.text().indexOf('/'));
                versions.add(versionNum);
            }

            return versions;

        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        }
    }

    public String getCompatibleBinaryVersion(String browserVersion) {
        logger.info(() -> String.format("Identifying Compatible %s version for Chrome Browser [%s] ", driverName, browserVersion));
        List<String> versions = getVersionNumbers(this.binaryDownloadUrl, this.driverName + "_" + this.osNameAndArch);

        String browserVersionPrefix = browserVersion.split("\\.")[0];
        String compatibleVersion;
        if (!versions.contains(browserVersion)) {
            versions.add(browserVersion);
            versions.sort(new VersionComparator());
            int index = versions.indexOf(browserVersion);
            String previousVersion = versions.get(index - 1);
            String nextVersion = versions.get(index + 1);
            compatibleVersion = nextVersion.startsWith(browserVersionPrefix) ? nextVersion : previousVersion;
        } else compatibleVersion = browserVersion;

        logger.info(() -> String.format("Using %s [%s] for Chrome Browser [%s] ", driverName, compatibleVersion, browserVersion));
        return compatibleVersion;
    }

}
