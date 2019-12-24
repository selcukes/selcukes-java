package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.DriverPoolException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class IExplorerBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/IEDriverServer_%s_%s.0.zip";

    public IExplorerBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
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

}