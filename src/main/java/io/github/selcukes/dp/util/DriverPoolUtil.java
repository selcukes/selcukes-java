package io.github.selcukes.dp.util;

import io.github.selcukes.dp.core.factory.*;
import io.github.selcukes.dp.enums.DriverType;
import io.github.selcukes.dp.enums.OSType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;

import static io.github.selcukes.dp.util.OptionalUtil.unwrap;

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
        FileHelper.createDirectory(binaryDownloadDirectory);
        return binaryDownloadDirectory;
    }

    public String downloadAndSetupBinaryPath() {
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
            case EDGE:
                this.binaryFactory = new EdgeBinary(release, targetArch);
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
