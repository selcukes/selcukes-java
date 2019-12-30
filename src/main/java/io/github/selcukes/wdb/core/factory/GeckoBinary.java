package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.enums.OSType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;

import java.net.MalformedURLException;
import java.net.URL;

public class GeckoBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/geckodriver-%s-%s.%s";

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.GECKODRIVER_URL,
                getBinaryVersion(),
                getBinaryVersion(),
                getBinaryEnvironment().getOsNameAndArch(),
                getCompressedBinaryType().getName()));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return getBinaryEnvironment().getOSType().equals(OSType.WIN) ? DownloaderType.ZIP : DownloaderType.TAR;
    }

    @Override
    public String getBinaryDriverName() {
        return "geckodriver";
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.FIREFOX;
    }

    @Override
    protected String getLatestRelease() {
        return getVersionNumberFromGit(MirrorUrls.GECKODRIVER_LATEST_RELEASE_URL);
    }
}