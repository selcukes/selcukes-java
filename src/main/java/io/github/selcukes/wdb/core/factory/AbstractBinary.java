package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
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
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private Optional<String> proxyUrl;
    protected String latestVersion;

    public AbstractBinary(String release, TargetArch targetArch, String proxyUrl) {

        this.release = Optional.ofNullable(release);
        this.targetArch = Optional.ofNullable(targetArch);
        this.proxyUrl = Optional.ofNullable(proxyUrl);
    }

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

    protected String getVersionNumber(InputStream inputStream, String matcher) {
        List<String> versions = new ArrayList<>();
        Map<String, String> versionMap = new HashMap<>();
        try {
            Document doc = parse(inputStream, null, "");
            Elements element = doc.select(
                "Key:contains(" + matcher + ")");
            for (Element e : element) {
                String key = e.text().substring(e.text().indexOf('/'));
                versionMap.put(key, e.text());
                String temp = e.text().substring(e.text().indexOf('/') + 1).replaceAll(matcher, "");
                if (temp.contains("standalone")) {
                    temp = temp.replaceAll("standalone-", "");
                }
                String versionNum = temp.substring(1, temp.length() - 4);

                versions.add(versionNum);
            }

            versions.sort(new VersionComparator());

            String version = versions.get(versions.size() - 1);
            latestVersion = unwrap(versionMap.entrySet().stream().filter(map -> map.getValue().contains(version)).findFirst()).getValue();

            return version;

        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        }
    }
}
