package io.github.selcukes.wdb.util;

import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.BinaryInfo;
import io.github.selcukes.wdb.core.factory.BinaryFactory;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.OSType;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class WebDriverBinaryUtil {
    private final Logger logger = LoggerFactory.getLogger(WebDriverBinaryUtil.class);
    private File binaryDownloadDirectory;
    private boolean strictDownload;
    private BinaryFactory binaryFactory;
    private static String webdriver = "webdriver";


    public WebDriverBinaryUtil(BinaryFactory binaryFactory, String downloadLocation, boolean strictDownload) {
        this.binaryFactory = binaryFactory;
        this.binaryDownloadDirectory = getBinaryDownloadDirectory(downloadLocation);
        this.strictDownload = strictDownload;
    }

    private File getBinaryDownloadDirectory(String downloadLocation) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + webdriver + File.separator);
        FileHelper.createDirectory(binaryDownloadDirectory);
        return binaryDownloadDirectory;
    }

    public BinaryInfo downloadAndSetupBinaryPath() {
        return setBinaryInfo(downloadAndExtract().configureBinary());
    }

    private BinaryInfo setBinaryInfo(String binProp) {
        return new BinaryInfo(binProp, getWebDriverBinary().getAbsolutePath());
    }

    private File getWebDriverBinary() {
        return new File(binaryDownloadDirectory.getAbsolutePath() +
            File.separator +
            binaryFactory.getBinaryDirectory() +
            File.separator +
            binaryFactory.getBinaryFileName());
    }

    private WebDriverBinaryUtil downloadAndExtract() {
        if (!strictDownload && getWebDriverBinary().exists()) {
            logger.info(() -> "Re-using an existing driver binary found at: " + getWebDriverBinary().getParent());
        } else {

            BinaryDownloadUtil.downloadBinary(binaryFactory.getDownloadURL(), binaryFactory.getCompressedBinaryFile());
            logger.info(() -> String.format("%s successfully downloaded to: %s", binaryFactory.getBinaryDriverName(), getWebDriverBinary().getParent()));

            if (binaryFactory.getCompressedBinaryType().equals(DownloaderType.JAR)) {
                FileHelper.createDirectory(new File(binaryDownloadDirectory + File.separator + binaryFactory.getBinaryDirectory()));
                try {
                    FileUtils.copyFile(binaryFactory.getCompressedBinaryFile(), getWebDriverBinary());
                } catch (IOException e) {
                    throw new WebDriverBinaryException(e);
                }
            } else decompressBinary();
            logger.info(() -> String.format("%s successfully extracted to: %s", binaryFactory.getBinaryDriverName(), getWebDriverBinary().getAbsolutePath()));
        }
        return this;
    }

    private void decompressBinary() {
        final File decompressedBinary = FileExtractUtil.extractFile(
            binaryFactory.getCompressedBinaryFile(),
            new File(binaryDownloadDirectory + File.separator + binaryFactory.getBinaryDirectory()),
            binaryFactory.getCompressedBinaryType());
        logger.debug(() -> "Deleting compressed file:" + binaryFactory.getCompressedBinaryFile().getAbsolutePath());
        binaryFactory.getCompressedBinaryFile().deleteOnExit();
        if (Objects.equals(binaryFactory.getBinaryEnvironment().getOSType(), OSType.LINUX))
            FileHelper.setFileExecutable(decompressedBinary.getAbsolutePath());
    }

    private String configureBinary() {
        StringBuilder binaryPropertyName = new StringBuilder();

        binaryPropertyName.append(webdriver)
            .append(".")
            .append(binaryFactory.getDriverType().getName())
            .append(".driver");

        System.setProperty(binaryPropertyName.toString(), getWebDriverBinary().getAbsolutePath());
        return binaryPropertyName.toString();
    }
}
