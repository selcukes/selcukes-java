/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.wdb.version;

import io.github.selcukes.collections.StringHelper;
import io.github.selcukes.commons.exec.ExecResults;
import io.github.selcukes.commons.exec.Shell;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.os.Platform;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.selcukes.wdb.util.VersionHelper.versionsList;

public class VersionDetector {
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
        Optional<String> version = CacheManager.resolveVersion(driverName);
        if (version.isPresent()) {
            String cacheVersion = version.get();
            if (!cacheVersion.isBlank()) {
                logger.info(() -> String.format("Using cached %s [%s] version", driverName, cacheVersion));
                return cacheVersion;
            }
        }
        if (Platform.isLinux()) {
            Shell shell = new Shell();
            String browserName = driverName.contains("chrome") ? "google-chrome" : "microsoft-edge";
            String command = browserName + " --version";
            String queryResult = shell.runCommand(command).getOutput().get(0);
            String browserVersion = StringHelper.extractVersionNumber(queryResult);
            logger.info(() -> "Browser Version Number: " + browserVersion);
            return getCompatibleBinaryVersion(browserVersion);
        } else {
            return getBrowserVersionFromCommand(getQuery());
        }
    }

    private String getQuery() {
        String wmicQuery = "wmic datafile where name='%s' get version";
        Map<String, String> browserPath = Map.of(
            "chromedriver", "C:\\\\Program Files (x86)\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe",
            "geckodriver", "C:\\\\program files\\\\Mozilla Firefox\\\\firefox.exe",
            "msedgedriver", "C:\\\\Program Files (x86)\\\\Microsoft\\\\Edge\\\\Application\\\\msedge.exe",
            "IEDriverServer", "C:\\\\Program Files\\\\Internet Explorer\\\\iexplore.exe");
        return String.format(wmicQuery, browserPath.get(driverName));
    }

    private String getBrowserVersionFromCommand(String regQuery) {
        Shell shell = new Shell();
        ExecResults execResults = shell.runCommand(regQuery);
        if (driverName.contains("chrome") && execResults.getOutput().get(2).isEmpty()) {
            execResults = shell.runCommand(regQuery.replace(" (x86)", ""));
        }
        String[] words = execResults.getOutput().get(2).split(" ");
        String browserVersion = words[words.length - 1];

        logger.info(() -> "Browser Version Number: " + browserVersion);

        return getCompatibleBinaryVersion(browserVersion);
    }

    public String getCompatibleBinaryVersion(String browserVersion) {
        logger.info(
            () -> String.format("Identifying Compatible %s version for Browser [%s] ", driverName, browserVersion));
        String matcher = this.driverName + "_" + this.osNameAndArch;
        String expression = "//Key";
        if (this.driverName.contains("edge")) {
            expression = "//Blob/Name";
            matcher = matcher.substring(2);
        }
        List<String> versions = versionsList(this.binaryDownloadUrl, expression, matcher);
        if (versions.isEmpty()) {
            logger.warn(() -> "Failed Identifying Compatible Version. Downloading Stable version.");
            return "";
        }
        String browserVersionPrefix = browserVersion.split("\\.")[0];
        String compatibleVersion;
        if (!versions.contains(browserVersion)) {
            versions.add(browserVersion);
            versions.sort(new VersionComparator());
            int index = versions.indexOf(browserVersion);
            if (index == versions.size() - 1) {
                compatibleVersion = (index == 0) ? versions.get(index) : versions.get(index - 1);
            } else {
                String previousVersion = versions.get(index - 1);
                String nextVersion = versions.get(index + 1);
                compatibleVersion = nextVersion.startsWith(browserVersionPrefix) ? nextVersion : previousVersion;
            }
        } else {
            compatibleVersion = browserVersion;
        }

        logger.info(
            () -> String.format("Using %s [%s] for Browser [%s] ", driverName, compatibleVersion, browserVersion));
        CacheManager.createCache(driverName, compatibleVersion);
        return compatibleVersion;
    }

}
