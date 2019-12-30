package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.util.HttpUtils;
import io.github.selcukes.wdb.util.Platform;
import io.github.selcukes.wdb.util.VersionComparator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.*;

import static io.github.selcukes.wdb.util.OptionalUtil.orElse;
import static io.github.selcukes.wdb.util.OptionalUtil.unwrap;
import static org.jsoup.Jsoup.parse;

abstract class AbstractBinary implements BinaryFactory {
    private Optional<String> release = Optional.empty();
    private Optional<TargetArch> targetArch = Optional.empty();
    private Optional<String> proxyUrl = Optional.empty();
    protected String latestVersionUrl;

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


    protected abstract String getLatestRelease();

    protected String getProxy() {
        return unwrap(proxyUrl);
    }

    protected String getVersionNumberFromGit(String binaryDownloadUrl) {
        final String releaseLocation = HttpUtils.getLocation(binaryDownloadUrl, getProxy());

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf('/') + 1);
    }

    protected String getVersionNumberFromXML(String binaryDownloadUrl, String matcher) {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(binaryDownloadUrl, getProxy());
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
            latestVersionUrl = unwrap(versionMap.entrySet().stream().filter(map -> map.getValue().contains(version)).findFirst()).getValue();

            return version;

        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public void setVersion(String version) {
        this.release = Optional.ofNullable(version);
    }

    @Override
    public void setTargetArch(TargetArch targetArch) {
        this.targetArch = Optional.ofNullable(targetArch);
    }

    public Optional<TargetArch> getTargetArch() {
        return this.targetArch;
    }

    @Override
    public void setProxy(String proxy) {
        this.proxyUrl = Optional.ofNullable(proxy);
    }
}
