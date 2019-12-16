package io.github.selcukes.dp.util;

import io.github.selcukes.dp.core.factory.BinaryFactory;
import io.github.selcukes.dp.core.factory.ChromeBinary;
import io.github.selcukes.dp.core.factory.GeckoBinary;
import io.github.selcukes.dp.core.factory.IExplorerBinary;
import io.github.selcukes.dp.enums.DriverType;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class DriverPoolUtil {
    private final Logger logger = Logger.getLogger(DriverPoolUtil.class.getName());

    private DriverType driverType;
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private File binaryDownloadDirectory;
    private BinaryFactory binaryFactory;
    private static String webdrivers = "webdrivers";

    public DriverPoolUtil(DriverType driverType, String release, TargetArch targetArch, String downloadLocation) {
        this.driverType = driverType;
        this.release = Optional.ofNullable(release);
        this.targetArch = Optional.ofNullable(targetArch);
        this.binaryDownloadDirectory = getBinaryDownloadDirectory(downloadLocation);
    }

    private File getBinaryDownloadDirectory(String downloadLocation) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + webdrivers + File.separator);
        try {

            FileHelper.createDirectory(binaryDownloadDirectory);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return binaryDownloadDirectory;
    }

    public void downloadAndSetupBinaryPath() {
        switch (driverType) {
            case CHROME:

                this.binaryFactory = new ChromeBinary(release, targetArch);
                break;

            case FIREFOX:
                this.binaryFactory = new GeckoBinary(release, targetArch);
                break;

            case IEXPLORER:
                this.binaryFactory = new IExplorerBinary(release, targetArch);
                break;
            default:
                throw new DriverPoolException(String.format("Currently %s not supported", driverType.toString()));
        }

        downloadBinaryAndConfigure();
    }

    private File getWebDriverBinary() {
        return new File(binaryDownloadDirectory.getAbsolutePath() +
                File.separator +
                binaryFactory.getBinaryDirectory() +
                File.separator +
                binaryFactory.getBinaryFileName());
    }

    private void downloadBinaryAndConfigure() {
        if (getWebDriverBinary().exists()) {
            logger.info("Re-using an existing driver binary found at: " + getWebDriverBinary().getParent());
        } else {

            BinaryDownloadUtil.downloadBinary(binaryFactory.getDownloadURL().get(), binaryFactory.getCompressedBinaryFile());

            logger.info(() -> String.format("%s successfully downloaded to: %s", binaryFactory.getBinaryDriverName(), getWebDriverBinary().getParent()));

            decompressBinary();
        }
        configureBinary(driverType);


    }

    private File decompressBinary() {
        final File decompressedBinary = FileExtractUtil.extractFile(
                binaryFactory.getCompressedBinaryFile(),
                new File(binaryDownloadDirectory + File.separator + binaryFactory.getBinaryDirectory()),
                binaryFactory.getCompressedBinaryType());
        if (binaryFactory.getBinaryEnvironment().getOSType().equals(OSType.LINUX))
            FileHelper.setFileExecutable(decompressedBinary.getAbsolutePath());
        return decompressedBinary;
    }

    private void configureBinary(DriverType driverType) {
        StringBuilder binaryPropertyName = new StringBuilder();

        binaryPropertyName.append(webdrivers)
                .append(".")
                .append(driverType.getName())
                .append(".driver");

        System.setProperty(binaryPropertyName.toString(), getWebDriverBinary().getAbsolutePath());
        logger.info("Property set " + System.getProperty(binaryPropertyName.toString()));
    }
}
