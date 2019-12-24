package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.MirrorUrls;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.HttpUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class GeckoBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/geckodriver-%s-%s.%s";
    private Optional<TargetArch> targetArch;

    public GeckoBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        super(release, targetArch);
        this.targetArch = targetArch;
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
                getBinaryEnvironment().getOSType().equals(OSType.WIN) ? DownloaderType.ZIP.getName() : DownloaderType.TAR.getName())));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        return targetArch.map(arch -> Environment.create(arch.getValue())).orElseGet(Environment::create);
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
    String getLatestRelease() {
        final String releaseLocation = HttpUtils.getLocation(MirrorUrls.GECKODRIVER_LATEST_RELEASE_URL, getProxy());

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf('/') + 1);
    }
}