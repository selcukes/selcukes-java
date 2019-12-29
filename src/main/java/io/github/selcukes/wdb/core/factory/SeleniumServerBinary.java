package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.util.HttpUtils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumServerBinary extends AbstractBinary {

    public SeleniumServerBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(MirrorUrls.SELENIUM_SERVER_URL + "/" + latestVersion);

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
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.SELENIUM_SERVER_LATEST_RELEASE_URL, null);
        return getVersionNumberFromXML(downloadStream, getBinaryDriverName());
    }

}