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
import java.util.Optional;

import static io.github.selcukes.dp.util.OptionalUtil.orElse;
import static io.github.selcukes.dp.util.OptionalUtil.unwrap;
import static org.jsoup.Jsoup.parse;

public class IExplorerBinary implements BinaryFactory {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/IEDriverServer_%s_%s.0.zip";
    private Optional<String> release;
    private Optional<TargetArch> targetArch;

    public IExplorerBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        this.release = orElse(release, getLatestRelease());
        this.targetArch = targetArch;
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.IEDRIVER_URL,
                getBinaryVersion(),
                getBinaryEnvironment().getArchitecture() == 64 ? "x64" : "Win32",
                getBinaryVersion())));

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
        return "IEDriverServer";
    }

    @Override
    public String getBinaryVersion() {
        return unwrap(release);
    }


    private String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.IEDRIVER_LATEST_RELEASE_URL, getProxy());

        try {

            Document doc = parse(downloadStream, null, "");
            Elements elements = doc.select(
                "Key:contains(IEDriverServer)");
            Element element = elements.get(elements.size() - 1);
            final String textContent = element.text();
            return textContent.substring(0, textContent.indexOf('/'));

        } catch (Exception e) {
            throw new DriverPoolException(e);
        }
    }
}