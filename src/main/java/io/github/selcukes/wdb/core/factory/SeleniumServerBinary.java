package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.Environment;
import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.DriverPoolException;
import io.github.selcukes.wdb.util.HttpUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static org.jsoup.Jsoup.parse;

public class SeleniumServerBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/selenium-server_%s.zip";
    public SeleniumServerBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
    }

    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.IEDRIVER_URL,
                getBinaryVersion(),
                getBinaryVersion())));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public String getBinaryDriverName() {
        return "selenium-server";
    }

    @Override
    protected String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.IEDRIVER_LATEST_RELEASE_URL, getProxy());

        try {
            Document doc = parse(downloadStream, null, "");
            Element element = doc.select(
                "Key:contains(selenium-server)").last();
            final String elementText = element.text();
            return elementText.substring(0, elementText.indexOf('/'));

        } catch (Exception e) {
            throw new DriverPoolException(e);
        }
    }

}
