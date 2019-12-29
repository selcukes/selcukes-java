package io.github.selcukes.wdb.core.factory;


import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;

import java.net.MalformedURLException;
import java.net.URL;

public class OperaBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/operadriver-%s.%s";


    public OperaBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.OPERA_DRIVER_URL,
                getBinaryVersion(),
                getBinaryEnvironment().getOsNameAndArch(),
                getCompressedBinaryType().getName()));
        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public String getBinaryDriverName() {
        return "operadriver";
    }

    @Override
    protected String getLatestRelease() {
        return getVersionNumberFromGit(MirrorUrls.OPERA_DRIVER_LATEST_RELEASE_URL);
    }
}
