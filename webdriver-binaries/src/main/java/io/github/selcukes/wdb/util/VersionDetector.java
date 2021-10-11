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

package io.github.selcukes.wdb.util;

import io.github.selcukes.commons.exception.WebDriverBinaryException;
import io.github.selcukes.commons.exec.ExecResults;
import io.github.selcukes.commons.exec.Shell;
import io.github.selcukes.commons.http.WebClient;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.os.Platform;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.*;

import static org.jsoup.Jsoup.parse;

public class VersionDetector {
    private static final String CH_KEY = "chKey";
    private static final String FF_KEY = "ffKey";
    private static final String IE_KEY = "ieKey";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String driverName;
    private final String osNameAndArch;
    private final String binaryDownloadUrl;

    public VersionDetector(String binaryDriverName, String osNameAndArch, String binaryDownloadUrl) {
        this.driverName = binaryDriverName;
        this.osNameAndArch = osNameAndArch;
        this.binaryDownloadUrl = binaryDownloadUrl;
    }

    public String getVersion() {
        Optional<String> regQuery = Optional.empty();

        if (Platform.isWindows()) {
            regQuery = Optional.ofNullable(getQuery(false));
        } else if (Platform.isLinux()) {

            Shell shell = new Shell();
            String command = "google-chrome --version";
            String queryResult = shell.runCommand(command).getOutput().get(0);
            String browserVersion = queryResult.replace("Google Chrome ", "").trim();
            logger.info(() -> "Browser Version Number: " + browserVersion);
            return getCompatibleBinaryVersion(browserVersion);
        }

        return regQuery.map(this::getBrowserVersionFromCommand).orElse(null);
    }

    private String getQuery(boolean fromReg) {
        if (driverName.contains("chrome"))
            return (fromReg) ? getRegQuery(CH_KEY) : getWMICQuery(CH_KEY);
        else if (driverName.contains("gecko"))
            return (fromReg) ? getRegQuery(FF_KEY) : getWMICQuery(FF_KEY);
        else if (driverName.contains("IE"))
            return (fromReg) ? getRegQuery(IE_KEY) : getWMICQuery(IE_KEY);
        else return null;
    }

    private String getRegQuery(String key) {
        String regQuery = "reg query \"%s\" /v version";
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put(CH_KEY, "HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon");
        keyMap.put(FF_KEY, "HKLM\\Software\\Mozilla\\Mozilla Firefox");
        keyMap.put(IE_KEY, "HKLM\\Software\\Microsoft\\Internet Explorer");
        return String.format(regQuery, keyMap.get(key));
    }

    private String getWMICQuery(String key) {
        String wmicQuery = "wmic datafile where name='%s' get version";
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put(CH_KEY, "C:\\\\Program Files (x86)\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe");
        keyMap.put(FF_KEY, "C:\\\\program files\\\\Mozilla Firefox\\\\firefox.exe");
        keyMap.put(IE_KEY, "C:\\\\Program Files\\\\Internet Explorer\\\\iexplore.exe");
        return String.format(wmicQuery, keyMap.get(key));
    }

    private String getBrowserVersionFromCommand(String regQuery) {
        Shell shell = new Shell();
        ExecResults execResults = shell.runCommand(regQuery);
        if (driverName.contains("chrome") && execResults.getOutput().get(2).isEmpty())
            execResults = shell.runCommand(regQuery.replace(" (x86)", ""));
        String[] words = execResults.getOutput().get(2).split(" ");
        String browserVersion = words[words.length - 1];

        logger.info(() -> "Browser Version Number: " + browserVersion);

        return getCompatibleBinaryVersion(browserVersion);
    }

    public List<String> getBinaryVersions(String binaryDownloadUrl, String matcher) {
        WebClient client = new WebClient(binaryDownloadUrl);
        final InputStream downloadStream = client.sendRequest().getResponseStream();
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
        List<String> versions = getBinaryVersions(this.binaryDownloadUrl, this.driverName + "_" + this.osNameAndArch);

        String browserVersionPrefix = browserVersion.split("\\.")[0];
        String compatibleVersion;
        if (!versions.contains(browserVersion)) {
            versions.add(browserVersion);
            versions.sort(new VersionComparator());
            int index = versions.indexOf(browserVersion);
            if (index == versions.size() - 1) {
                compatibleVersion = versions.get(index - 1);

            } else {
                String previousVersion = versions.get(index - 1);
                String nextVersion = versions.get(index + 1);
                compatibleVersion = nextVersion.startsWith(browserVersionPrefix) ? nextVersion : previousVersion;
            }
        } else compatibleVersion = browserVersion;

        logger.info(() -> String.format("Using %s [%s] for Chrome Browser [%s] ", driverName, compatibleVersion, browserVersion));
        return compatibleVersion;
    }

}
