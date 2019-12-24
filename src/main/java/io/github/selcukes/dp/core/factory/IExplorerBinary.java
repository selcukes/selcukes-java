package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.MirrorUrls;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.HttpUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static org.jsoup.Jsoup.parse;

public class IExplorerBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/IEDriverServer_%s_%s.0.zip";

    public IExplorerBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        super(release, targetArch);
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
    public String getBinaryDriverName() {
        return "IEDriverServer";
    }

    @Override
    String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.IEDRIVER_LATEST_RELEASE_URL, getProxy());

        try {
            Document doc = parse(downloadStream, null, "");
            Element element = doc.select(
                "Key:contains(IEDriverServer)").last();
            final String elementText = element.text();
            return elementText.substring(0, elementText.indexOf('/'));

        } catch (Exception e) {
            throw new DriverPoolException(e);
        }
    }
}