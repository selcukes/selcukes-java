package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.URLLookup;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.HttpUtils;
import io.github.selcukes.dp.util.TempFileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

public class GeckoBinary implements BinaryFactory {
    private final String BINARY_DOWNLOAD_URL_TAR_PATTERN = "%s/%s/geckodriver-%s-%s.tar.gz";
    private final String BINARY_DOWNLOAD_URL_ZIP_PATTERN = "%s/%s/geckodriver-%s-%s.zip";
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private Function<Environment, String> binaryDownloadPattern = (osEnvironment) -> {
        if (osEnvironment.getOSType().equals(OSType.WIN)) {
            return BINARY_DOWNLOAD_URL_ZIP_PATTERN;
        } else {
            return BINARY_DOWNLOAD_URL_TAR_PATTERN;
        }
    };
    private Function<Environment, String> osNameAndArc = (osEnvironment) -> {
        if (osEnvironment.getOSType().equals(OSType.MAC)) {
            return "macos";
        } else {
            return osEnvironment.getOsNameAndArch();
        }
    };
    private Function<Environment, String> compressedBinaryExt = (osEnvironment) -> osEnvironment.getOSType().equals(OSType.WIN) ? "zip" : "tar.gz";


    public GeckoBinary(Optional<String> release, Optional<TargetArch> targetArch) {
        this.release = release;
        if(!this.release.isPresent())
            this.release=Optional.of(getLatestRelease());
        this.targetArch=targetArch;
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                    binaryDownloadPattern.apply(getBinaryEnvironment()),
                    URLLookup.GECKODRIVER_URL,
                    release.get(),
                    release.get(),
                    osNameAndArc.apply(getBinaryEnvironment()))));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        return targetArch.isPresent() ? Environment.create(targetArch.get().getValue()) : Environment.create();
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/geckodriver_%s.%s",
                TempFileUtil.getTempDirectory(),
                release.get(),
                compressedBinaryExt.apply(getBinaryEnvironment())));
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return getBinaryEnvironment().getOSType().equals(OSType.WIN) ? DownloaderType.ZIP : DownloaderType.TAR;
    }

    @Override
    public String getBinaryFileName() {
        return getBinaryEnvironment().getOSType().equals(OSType.WIN) ? "geckodriver.exe" : "geckodriver";
    }

    public String getBinaryDirectory() {
        return "geckodriver_" + release.orElse("");
    }

    @Override
    public String getBinaryDriverName() {
        return "GeckoDriver";
    }

    @Override
    public String getBinaryVersion() {
        return release.get();
    }


    private String getLatestRelease() {
        final String releaseLocation = HttpUtils.getLocation(URLLookup.GECKODRIVER_LATEST_RELEASE_URL);

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf("/") + 1);
    }
}