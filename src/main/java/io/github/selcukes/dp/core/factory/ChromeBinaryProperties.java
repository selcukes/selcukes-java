package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.URLLookup;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.OsType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.BinaryDownloadUtil;
import io.github.selcukes.dp.util.TempFileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The type Chrome binary properties.
 */
public class ChromeBinaryProperties implements BinaryProperties {
    private final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/chromedriver_%s.zip";
    private String release;
    private TargetArch targetArch;

    private ChromeBinaryProperties() {
        release = getLatestRelease();

        if (release.length() == 0) {
            throw new DriverPoolException("Unable to read the latest ChromeDriver release from: " + URLLookup.CHROMEDRIVER_LATEST_RELEASE_URL);
        }
    }

    private ChromeBinaryProperties(String release) {
        this.release = release;
    }

    /**
     * For latest release chrome binary properties.
     *
     * @return the chrome binary properties
     */
    public static ChromeBinaryProperties forLatestRelease() {
        return new ChromeBinaryProperties();
    }

    /**
     * For previous release chrome binary properties.
     *
     * @param release the release
     * @return the chrome binary properties
     */
    public static ChromeBinaryProperties forPreviousRelease(String release) {
        return new ChromeBinaryProperties(release);
    }

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    URLLookup.CHROMEDRIVER_URL,
                    release,
                    getBinaryEnvironment().getOsNameAndArch()));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        final Environment environment = Environment.create();

        return targetArch != null
                ? Environment.create(targetArch.getValue())
                : environment.getOsType().equals(OsType.WIN)
                ? Environment.create(32)
                : environment;
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/chromedriver_%s.zip", TempFileUtil.getTempDirectory(), release));
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return DownloaderType.ZIP;
    }

    @Override
    public String getBinaryFilename() {
        return getBinaryEnvironment().getOsType().equals(OsType.WIN) ? "chromedriver.exe" : "chromedriver";
    }

    public String getBinaryDirectory() {
        return release != null ? "chromedriver_" + release : "chromedriver";
    }

    @Override
    public String getBinaryDriverName() {
        return "ChromeDriver";
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
        try {
            return BinaryDownloadUtil.downloadAndReadFile(new URL(URLLookup.CHROMEDRIVER_LATEST_RELEASE_URL));
        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }
}