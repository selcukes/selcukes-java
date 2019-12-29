package io.github.selcukes.wdb.util;

import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.BinaryInfo;
import io.github.selcukes.wdb.core.factory.*;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.enums.OSType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class WebDriverBinaryUtil {
    private final Logger logger = LoggerFactory.getLogger(WebDriverBinaryUtil.class);

    private DriverType driverType;
    private String release;
    private TargetArch targetArch;
    private String proxyUrl;
    private File binaryDownloadDirectory;
    private BinaryFactory binaryFactory;
    private static String webdriver = "webdriver";

    public WebDriverBinaryUtil(DriverType driverType, String release, TargetArch targetArch, String downloadLocation, String proxyUrl) {
        this.driverType = driverType;
        this.release = release;
        this.targetArch = targetArch;
        this.binaryDownloadDirectory = getBinaryDownloadDirectory(downloadLocation);
        this.proxyUrl = proxyUrl;
    }

    private File getBinaryDownloadDirectory(String downloadLocation) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + webdriver + File.separator);
        FileHelper.createDirectory(binaryDownloadDirectory);
        return binaryDownloadDirectory;
    }

    public BinaryInfo downloadAndSetupBinaryPath() {
        switch (driverType) {
            case CHROME:
                this.binaryFactory = new ChromeBinary(release, targetArch, proxyUrl);
                break;
            case FIREFOX:
                this.binaryFactory = new GeckoBinary(release, targetArch, proxyUrl);
                break;
            case IEXPLORER:
                this.binaryFactory = new IExplorerBinary(release, targetArch, proxyUrl);
                break;
            case EDGE:
                this.binaryFactory = new EdgeBinary(release, targetArch, proxyUrl);
                break;
            case OPERA:
                this.binaryFactory = new OperaBinary(release, targetArch, proxyUrl);
                break;
            case GRID:
                this.binaryFactory = new SeleniumServerBinary(release, targetArch, proxyUrl);
                break;
            default:
                throw new WebDriverBinaryException(String.format("Currently %s not supported", driverType.toString()));
        }

        return setBinaryInfo(downloadAndExtract().configureBinary(driverType));
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
        if (getWebDriverBinary().exists()) {
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
        }
        return this;


    }

    private void decompressBinary() {
        final File decompressedBinary = FileExtractUtil.extractFile(
            binaryFactory.getCompressedBinaryFile(),
            new File(binaryDownloadDirectory + File.separator + binaryFactory.getBinaryDirectory()),
            binaryFactory.getCompressedBinaryType());
        if (binaryFactory.getBinaryEnvironment().getOSType().equals(OSType.LINUX))
            FileHelper.setFileExecutable(decompressedBinary.getAbsolutePath());
    }

    private String configureBinary(DriverType driverType) {
        StringBuilder binaryPropertyName = new StringBuilder();

        binaryPropertyName.append(webdriver)
            .append(".")
            .append(driverType.getName())
            .append(".driver");

        System.setProperty(binaryPropertyName.toString(), getWebDriverBinary().getAbsolutePath());
        return binaryPropertyName.toString();
    }
}
