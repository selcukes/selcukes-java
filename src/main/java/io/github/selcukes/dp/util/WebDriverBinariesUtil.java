package io.github.selcukes.dp.util;

import io.github.selcukes.dp.core.factory.*;
import io.github.selcukes.dp.enums.DriverType;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.File;
import java.util.logging.Logger;

import static io.github.selcukes.dp.util.OptionalUtil.unwrap;

public class WebDriverBinariesUtil {
    private final Logger logger = Logger.getLogger(WebDriverBinariesUtil.class.getName());

    private DriverType driverType;
    private String release;
    private TargetArch targetArch;
    private String proxyUrl;
    private File binaryDownloadDirectory;
    private BinaryFactory binaryFactory;
    private static String webdrivers = "webdrivers";

    public WebDriverBinariesUtil(DriverType driverType, String release, TargetArch targetArch, String downloadLocation, String proxyUrl) {
        this.driverType = driverType;
        this.release = release;
        this.targetArch = targetArch;
        this.binaryDownloadDirectory = getBinaryDownloadDirectory(downloadLocation);
        this.proxyUrl = proxyUrl;
    }

    private File getBinaryDownloadDirectory(String downloadLocation) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + webdrivers + File.separator);
        FileHelper.createDirectory(binaryDownloadDirectory);
        return binaryDownloadDirectory;
    }

    public String downloadAndSetupBinaryPath() {
        switch (driverType) {
            case CHROME:

                this.binaryFactory = new ChromeBinary(release, targetArch,proxyUrl);
                break;

            case FIREFOX:
                this.binaryFactory = new GeckoBinary(release, targetArch,proxyUrl);
                break;

            case IEXPLORER:
                this.binaryFactory = new IExplorerBinary(release, targetArch,proxyUrl);
                break;
            case EDGE:
                this.binaryFactory = new EdgeBinary(release, targetArch,proxyUrl);
                break;
            default:
                throw new DriverPoolException(String.format("Currently %s not supported", driverType.toString()));
        }

        return downloadBinaryAndConfigure();
    }

    private File getWebDriverBinary() {
        return new File(binaryDownloadDirectory.getAbsolutePath() +
            File.separator +
            binaryFactory.getBinaryDirectory() +
            File.separator +
            binaryFactory.getBinaryFileName());
    }

    private String downloadBinaryAndConfigure() {
        if (getWebDriverBinary().exists()) {
            logger.info("Re-using an existing driver binary found at: " + getWebDriverBinary().getParent());
        } else {

            BinaryDownloadUtil.downloadBinary(unwrap(binaryFactory.getDownloadURL()), binaryFactory.getCompressedBinaryFile());

            logger.info(() -> String.format("%s successfully downloaded to: %s", binaryFactory.getBinaryDriverName(), getWebDriverBinary().getParent()));

            decompressBinary();
        }
        return configureBinary(driverType);


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

        binaryPropertyName.append(webdrivers)
            .append(".")
            .append(driverType.getName())
            .append(".driver");

        System.setProperty(binaryPropertyName.toString(), getWebDriverBinary().getAbsolutePath());
        return binaryPropertyName.toString();
    }
}
