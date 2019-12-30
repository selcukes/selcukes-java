package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;

import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumServerBinary extends AbstractBinary {

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(MirrorUrls.SELENIUM_SERVER_URL + "/" + latestVersionUrl);

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
        return "selenium-server-standalone";
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.GRID;
    }

    @Override
    protected String getLatestRelease() {
        return getVersionNumberFromXML(MirrorUrls.SELENIUM_SERVER_LATEST_RELEASE_URL, getBinaryDriverName());
    }

}