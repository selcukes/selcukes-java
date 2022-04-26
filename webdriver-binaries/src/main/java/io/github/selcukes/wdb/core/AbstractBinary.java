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

import io.github.selcukes.commons.http.Response;
import io.github.selcukes.commons.http.WebClient;
import io.github.selcukes.commons.os.Architecture;
import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.wdb.version.VersionDetector;

import java.util.Optional;

import static io.github.selcukes.wdb.util.OptionalUtil.orElse;
import static io.github.selcukes.wdb.util.OptionalUtil.unwrap;

abstract class AbstractBinary implements BinaryFactory {
    protected String latestVersionUrl;
    protected boolean isAutoDetectBrowserVersion = true;
    private Optional<String> release = Optional.empty();
    private Optional<Architecture> targetArch = Optional.empty();
    private Optional<String> proxyUrl = Optional.empty();

    @Override
    public Platform getBinaryEnvironment() {
        Platform platform = Platform.getPlatform();
        targetArch.ifPresent(arch -> platform.setArchitecture(arch.getValue()));
        return platform;
    }

    @Override
    public String getBinaryVersion() {
        return orElse(release, getLatestRelease());
    }

    @Override
    public void setVersion(String version) {
        this.release = Optional.ofNullable(version);
    }

    public Optional<Architecture> getTargetArch() {
        return this.targetArch;
    }

    @Override
    public void setTargetArch(Architecture targetArch) {
        this.targetArch = Optional.ofNullable(targetArch);
    }

    @Override
    public void browserVersion(boolean isAutoCheck) {
        isAutoDetectBrowserVersion = isAutoCheck;
    }

    protected abstract String getLatestRelease();

    protected String getProxy() {
        return unwrap(proxyUrl);
    }

    @Override
    public void setProxy(String proxy) {
        this.proxyUrl = Optional.ofNullable(proxy);
    }

    protected Response getHttpClient(String binaryDownloadUrl) {
        return new WebClient(binaryDownloadUrl, getProxy()).sendRequest();
    }

    protected String getVersionNumberFromGit(String binaryDownloadUrl) {
        final String releaseLocation = getHttpClient(binaryDownloadUrl).getHeader("location");

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf('/') + 1);
    }

    protected void setBrowserVersion(String url) {
        String localBrowserVersion = new VersionDetector(getBinaryDriverName(),
            getBinaryEnvironment().getOsNameAndArch(), url).getVersion();
        setVersion(localBrowserVersion);
    }

}
