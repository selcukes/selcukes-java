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

import io.github.selcukes.commons.os.Architecture;
import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.wdb.version.VersionDetector;

import java.util.Optional;

import static io.github.selcukes.wdb.util.VersionHelper.sendRequest;
import static java.util.Optional.ofNullable;

abstract class AbstractBinary implements BinaryFactory {
    protected boolean isAutoDetectBrowserVersion = true;
    private String release;
    private Architecture targetArch;
    private String proxyUrl;

    @Override
    public Platform getBinaryEnvironment() {
        Platform platform = Platform.getPlatform();
        getTargetArch().ifPresent(arch -> platform.setArchitecture(arch.getValue()));
        return platform;
    }

    @Override
    public String getBinaryVersion() {
        return ofNullable(release).orElse(getLatestRelease());
    }

    @Override
    public void setVersion(String version) {
        this.release = version;
    }

    public Optional<Architecture> getTargetArch() {
        return ofNullable(this.targetArch);
    }

    @Override
    public void setTargetArch(Architecture targetArch) {
        this.targetArch = targetArch;
    }

    @Override
    public void browserVersion(boolean isAutoCheck) {
        isAutoDetectBrowserVersion = isAutoCheck;
    }

    protected abstract String getLatestRelease();

    protected String getProxy() {
        return proxyUrl;
    }

    @Override
    public void setProxy(String proxy) {
        this.proxyUrl = proxy;
    }

    protected String getVersionNumberFromGit(String binaryDownloadUrl) {
        final String releaseLocation = sendRequest(binaryDownloadUrl, getProxy()).header("location");

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf('/') + 1);
    }

    protected String getVersionNumberFromXml(String binaryDownloadUrl) {
        return sendRequest(binaryDownloadUrl, getProxy()).body();
    }

    protected void setBrowserVersion(String url) {
        String localBrowserVersion = new VersionDetector(getBinaryDriverName(),
                getBinaryEnvironment().getOsNameAndArch(), url).getVersion();
        if (!localBrowserVersion.isEmpty()) {
            setVersion(localBrowserVersion);
        }
    }

}
