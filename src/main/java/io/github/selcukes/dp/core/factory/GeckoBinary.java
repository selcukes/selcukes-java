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
import java.util.function.Function;

import static io.github.selcukes.dp.util.OptionalUtil.OrElse;
import static io.github.selcukes.dp.util.OptionalUtil.unwrap;

public class GeckoBinary implements BinaryFactory {
    private static final String BINARY_DOWNLOAD_URL_TAR_PATTERN = "%s/%s/geckodriver-%s-%s.tar.gz";
    private static final String BINARY_DOWNLOAD_URL_ZIP_PATTERN = "%s/%s/geckodriver-%s-%s.zip";
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private Function<Environment, String> binaryDownloadPattern = osEnvironment -> {
        if (osEnvironment.getOSType().equals(OSType.WIN)) {
            return BINARY_DOWNLOAD_URL_ZIP_PATTERN;
        } else {
            return BINARY_DOWNLOAD_URL_TAR_PATTERN;
        }
    };
    private Function<Environment, String> osNameAndArc = osEnvironment -> {
        if (osEnvironment.getOSType().equals(OSType.MAC)) {
            return "macos";
        } else {
            return osEnvironment.getOsNameAndArch();
        }
    };

    public GeckoBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        this.release = OrElse(release, getLatestRelease());
        this.targetArch = targetArch;
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                    binaryDownloadPattern.apply(getBinaryEnvironment()),
                    MirrorUrls.GECKODRIVER_URL,
                    getBinaryVersion(),
                    getBinaryVersion(),
                    osNameAndArc.apply(getBinaryEnvironment()))));

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
        return "GeckoDriver";
    }

    @Override
    public String getBinaryVersion() {
        return unwrap(release);
    }


    private String getLatestRelease() {
        final String releaseLocation = HttpUtils.getLocation(MirrorUrls.GECKODRIVER_LATEST_RELEASE_URL);

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf('/') + 1);
    }
}