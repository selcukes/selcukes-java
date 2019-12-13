package io.github.selcukes.dp.core.factory;

import io.github.selcukes.dp.core.Environment;
import io.github.selcukes.dp.core.URLLookup;
import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;
import io.github.selcukes.dp.util.BinaryDownloadUtil;
import io.github.selcukes.dp.util.TempFileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Logger;

public class ChromeBinary implements BinaryFactory {
    private Logger logger=Logger.getLogger(ChromeBinary.class.getName());
    private final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/chromedriver_%s.zip";
    private Optional<String> release;
    private Optional<TargetArch> targetArch;


    public ChromeBinary(Optional<String> release, Optional<TargetArch> targetArch) {

        this.release = release;
        if(!this.release.isPresent())
            this.release=Optional.of(getLatestRelease());
            this.targetArch=targetArch;
    }


    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                    BINARY_DOWNLOAD_URL_PATTERN,
                    URLLookup.CHROMEDRIVER_URL,
                    release.get(),
                    getBinaryEnvironment().getOsNameAndArch())));

        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }

    @Override
    public Environment getBinaryEnvironment() {
        final Environment environment = Environment.create();


        return targetArch.isPresent()
                ? Environment.create(targetArch.get().getValue())
                : environment.getOSType().equals(OSType.WIN)
                ? Environment.create(TargetArch.X32.getValue())
                : environment;
    }

    @Override
    public File getCompressedBinaryFile() {
        return new File(String.format("%s/chromedriver_%s.zip", TempFileUtil.getTempDirectory(), release.get()));
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return DownloaderType.ZIP;
    }

    @Override
    public String getBinaryFileName() {
        return getBinaryEnvironment().getOSType().equals(OSType.WIN) ? "chromedriver.exe" : "chromedriver";
    }

    public String getBinaryDirectory() {

        return "chromedriver_"+release.orElse("");
    }

    @Override
    public String getBinaryDriverName() {
        return "ChromeDriver";
    }

    @Override
    public String getBinaryVersion() {
        return release.get();
    }


    private String getLatestRelease() {
        try {
            return BinaryDownloadUtil.downloadAndReadFile(new URL(URLLookup.CHROMEDRIVER_LATEST_RELEASE_URL));
        } catch (MalformedURLException e) {
            throw new DriverPoolException(e);
        }
    }
}