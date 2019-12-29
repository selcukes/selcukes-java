package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.OSType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.util.HttpUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class GeckoBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/geckodriver-%s-%s.%s";


    public GeckoBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
    }

    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.GECKODRIVER_URL,
                getBinaryVersion(),
                getBinaryVersion(),
                getBinaryEnvironment().getOsNameAndArch(),
                getCompressedBinaryType().getName())));

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
    protected String getLatestRelease() {
        final String releaseLocation = HttpUtils.getLocation(MirrorUrls.GECKODRIVER_LATEST_RELEASE_URL, getProxy());

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf('/') + 1);
    }
}