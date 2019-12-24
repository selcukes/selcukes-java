package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.MirrorUrls;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.BinaryDownloadUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class ChromeBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/chromedriver_%s.zip";

    private Optional<TargetArch> targetArch;


    public ChromeBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
        this.targetArch = Optional.ofNullable(targetArch);
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.CHROMEDRIVER_URL,
                getBinaryVersion(),
                getBinaryEnvironment().getOsNameAndArch())));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        final Environment environment = Environment.create();

        return targetArch.map(arch -> Environment.create(arch.getValue())).orElseGet(() -> environment.getOSType().equals(OSType.WIN)
            ? Environment.create(TargetArch.X32.getValue())
            : environment);
    }

    @Override
    public String getBinaryDriverName() {
        return "chromedriver";
    }

    @Override
    String getLatestRelease() {
        try {
            return BinaryDownloadUtil.downloadAndReadFile(new URL(MirrorUrls.CHROMEDRIVER_LATEST_RELEASE_URL));
        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }
}