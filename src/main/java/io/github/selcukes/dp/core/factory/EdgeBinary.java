package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.MirrorUrls;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.HttpUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.selcukes.dp.util.OptionalUtil.orElse;
import static io.github.selcukes.dp.util.OptionalUtil.unwrap;
import static org.jsoup.Jsoup.parse;

public class EdgeBinary implements BinaryFactory {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/edgedriver_%s.zip";
    private Optional<String> release;
    private Optional<TargetArch> targetArch;

    public EdgeBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        this.release = orElse(release, getLatestRelease());
        this.targetArch = targetArch;
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    MirrorUrls.EDGE_DRIVER_URL,
                    getBinaryVersion(),
                    getBinaryEnvironment().getOsNameAndArch()
            )));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        return targetArch.map(arch -> Environment.create(arch.getValue())).orElseGet(Environment::create);
    }

    @Override
    public String getBinaryDriverName() {
        return "msedgedriver";
    }

    @Override
    public String getBinaryVersion() {
        return unwrap(release);
    }


    private String getLatestRelease() {
        List<String> versionNumbers = new ArrayList<>();
        String latestVersion = null;
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.EDGE_DRIVER_LATEST_RELEASE_URL);
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
            throw new DriverPoolException(e);
        }
        return latestVersion;
    }
}
