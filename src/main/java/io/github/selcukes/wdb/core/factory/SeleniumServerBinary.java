package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.util.HttpUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.jsoup.Jsoup.parse;

public class SeleniumServerBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/selenium-server-%s.jar";
    private String latestVersion;

    public SeleniumServerBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
    }

    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.IEDRIVER_URL,
                getBinaryVersion().contains("alpha") ? latestVersion : getBinaryVersion(),
                getBinaryVersion())));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public String getBinaryFileName() {
        return getBinaryDriverName() + ".jar";
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return DownloaderType.JAR;
    }

    @Override
    public String getBinaryDriverName() {
        return "selenium-server";
    }

    @Override
    protected String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.IEDRIVER_LATEST_RELEASE_URL, null);
        List<String> list = new ArrayList<>();
        Set<String> version = new HashSet<>();
        try {
            Document doc = parse(downloadStream, null, "");
            Elements element = doc.select(
                "Key:contains(selenium-server)");
            for (Element e : element) {

                version.add(e.text().substring(0, e.text().indexOf('/')));
                String versionNum = e.text().substring(e.text().indexOf('/') + 1);

                list.add(versionNum);
            }
            List<String> sortedList = list.stream().sorted().collect(Collectors.toList());


            latestVersion = version.stream().sorted().collect(Collectors.toList()).get(version.size() - 1);
            String filterString = "selenium-server-" + latestVersion.substring(0, 3);

            sortedList.removeIf(check -> !check.contains(filterString));

            String temp = sortedList.get(sortedList.size() - 1);
            return temp.substring(temp.indexOf(latestVersion.charAt(0)), temp.length() - 4);

        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        }
    }

}
