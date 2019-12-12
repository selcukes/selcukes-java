package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.URLLookup;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.OsType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.HttpUtils;
import io.github.selcukes.dp.util.TempFileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;

/**
 * The type Gecko binary properties.
 */
public class GeckoBinaryProperties implements BinaryProperties {
    private final String BINARY_DOWNLOAD_URL_TAR_PATTERN = "%s/%s/geckodriver-%s-%s.tar.gz";
    private final String BINARY_DOWNLOAD_URL_ZIP_PATTERN = "%s/%s/geckodriver-%s-%s.zip";
    private String release;
    private TargetArch targetArch;
    private Function<Environment, String> binaryDownloadPattern = (osEnvironment) -> {
        if (osEnvironment.getOsType().equals(OsType.WIN)) {
            return BINARY_DOWNLOAD_URL_ZIP_PATTERN;
        } else {
            return BINARY_DOWNLOAD_URL_TAR_PATTERN;
        }
    };
    private Function<Environment, String> osNameAndArc = (osEnvironment) -> {
        if (osEnvironment.getOsType().equals(OsType.MAC)) {
            return "macos";
        } else {
            return osEnvironment.getOsNameAndArch();
        }
    };
    private Function<Environment, String> compressedBinaryExt = (osEnvironment) -> osEnvironment.getOsType().equals(OsType.WIN) ? "zip" : "tar.gz";

    private GeckoBinaryProperties() {
        release = getLatestRelease();

        if (release.length() == 0) {
            throw new DriverPoolException("Unable to read the latest GeckoDriver release from: " + URLLookup.GECKODRIVER_LATEST_RELEASE_URL);
        }
    }

    private GeckoBinaryProperties(String release) {
        this.release = release;
    }

    /**
     * For latest release gecko binary properties.
     *
     * @return the gecko binary properties
     */
    public static GeckoBinaryProperties forLatestRelease() {
        return new GeckoBinaryProperties();
    }

    /**
     * For previous release gecko binary properties.
     *
     * @param release the release
     * @return the gecko binary properties
     */
    public static GeckoBinaryProperties forPreviousRelease(String release) {
        return new GeckoBinaryProperties(release);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                    binaryDownloadPattern.apply(getBinaryEnvironment()),
                    URLLookup.GECKODRIVER_URL,
                    release,
                    release,
                    osNameAndArc.apply(getBinaryEnvironment())));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        return targetArch != null ? Environment.create(targetArch.getValue()) : Environment.create();
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/geckodriver_%s.%s",
                TempFileUtil.getTempDirectory(),
                release,
                compressedBinaryExt.apply(getBinaryEnvironment())));
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return getBinaryEnvironment().getOsType().equals(OsType.WIN) ? DownloaderType.ZIP : DownloaderType.TAR;
    }

    @Override
    public String getBinaryFilename() {
        return getBinaryEnvironment().getOsType().equals(OsType.WIN) ? "geckodriver.exe" : "geckodriver";
    }

    public String getBinaryDirectory() {
        return release != null ? "geckodriver_" + release : "geckodriver";
    }

    @Override
    public String getBinaryDriverName() {
        return "GeckoDriver";
    }

    @Override
    public String getBinaryVersion() {
        return release;
    }

    @Override
    public void setBinaryArchitecture(TargetArch targetArch) {
        this.targetArch = targetArch;
    }

    private String getLatestRelease() {
        final String releaseLocation = HttpUtils.getLocation(URLLookup.GECKODRIVER_LATEST_RELEASE_URL);

        if (releaseLocation == null || releaseLocation.length() < 2 || !releaseLocation.contains("/")) {
            return "";
        }
        return releaseLocation.substring(releaseLocation.lastIndexOf("/") + 1);
    }
}