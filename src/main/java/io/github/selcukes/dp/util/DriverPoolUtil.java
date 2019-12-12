package io.github.selcukes.dp.util;

import io.github.selcukes.dp.core.BinaryInfo;
import io.github.selcukes.dp.core.factory.BinaryProperties;
import io.github.selcukes.dp.core.factory.ChromeBinaryProperties;
import io.github.selcukes.dp.core.factory.GeckoBinaryProperties;
import io.github.selcukes.dp.core.factory.IExplorerBinaryProperties;
import io.github.selcukes.dp.enums.DriverType;
import io.github.selcukes.dp.enums.TargetArch;
import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.System.setProperty;

/**
 * The type Driver pool util.
 */
public class DriverPoolUtil {
    private final Logger logger = Logger.getLogger(DriverPoolUtil.class.getName());

    private DriverType driverType;
    private Optional<String> release;
    private Optional<TargetArch> targetArch;
    private File binaryDownloadDirectory;
    private BinaryProperties binaryProperties;

    /**
     * Instantiates a new Driver pool util.
     *
     * @param driverType       the driver type
     * @param release          the release
     * @param targetArch       the target arch
     * @param downloadLocation the download location
     */
    public DriverPoolUtil(DriverType driverType, String release, TargetArch targetArch, String downloadLocation) {
        this.driverType = driverType;
        this.release = Optional.ofNullable(release);
        this.targetArch = Optional.ofNullable(targetArch);
        this.binaryDownloadDirectory = getBinaryDownloadDirectory(downloadLocation);
    }

    private File getBinaryDownloadDirectory(String downloadLocation) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + "webdrivers" + File.separator);
        try {

            FileHelper.createDirectory(binaryDownloadDirectory);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return binaryDownloadDirectory;
    }

    /**
     * Download and setup binary path.
     */
    public void downloadAndSetupBinaryPath() {
        switch (driverType) {
            case CHROME:

                this.binaryProperties = this.release.map(ChromeBinaryProperties::forPreviousRelease).orElseGet(ChromeBinaryProperties::forLatestRelease);
                break;

            case FIREFOX:
                this.binaryProperties = this.release.map(GeckoBinaryProperties::forPreviousRelease).orElseGet(GeckoBinaryProperties::forLatestRelease);
                break;

            case IEXPLORER:
                this.binaryProperties = this.release.map(IExplorerBinaryProperties::forPreviousRelease).orElseGet(IExplorerBinaryProperties::forLatestRelease);
                break;
            default:
                throw new DriverPoolException(String.format("Currently %s not supported", driverType.toString()));
        }
        targetArch.ifPresent(arch -> this.binaryProperties.setBinaryArchitecture(arch));
        downloadBinaryAndConfigure();
    }

    private File getWebDriverBinary() {
        return new File(binaryDownloadDirectory.getAbsolutePath() +
                File.separator +
                binaryProperties.getBinaryDirectory() +
                File.separator +
                binaryProperties.getBinaryFilename());
    }

    private BinaryInfo downloadBinaryAndConfigure() {
        if (getWebDriverBinary().exists()) {
            logger.info("Re-using an existing driver binary found at: " + getWebDriverBinary().getParent());
        } else {
            BinaryDownloadUtil.downloadBinary(binaryProperties.getDownloadURL(), binaryProperties.getCompressedBinaryFile());

            logger.info(() -> String.format("%s successfully downloaded to: %s", binaryProperties.getBinaryDriverName(), getWebDriverBinary().getParent()));

            decompressBinary();
        }
        configureBinary(driverType);

        return new BinaryInfo(binaryProperties.getBinaryDriverName(), binaryProperties.getBinaryVersion(), getWebDriverBinary().getAbsolutePath());
    }

    private File decompressBinary() {
        final File decompressedBinary = FileExtractUtil.extractFile(
                binaryProperties.getCompressedBinaryFile(),
                new File(binaryDownloadDirectory + File.separator + binaryProperties.getBinaryDirectory()),
                binaryProperties.getCompressedBinaryType());

        switch (binaryProperties.getBinaryEnvironment().getOsType()) {
            case MAC:
            case LINUX:
                FileHelper.setFileExecutable(decompressedBinary.getAbsolutePath());
        }
        return decompressedBinary;
    }

    private void configureBinary(DriverType driverType) {
        switch (driverType) {
            case CHROME: {
                setProperty("webdriver.chrome.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
            case FIREFOX: {
                setProperty("webdriver.gecko.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
            case IEXPLORER: {
                setProperty("webdriver.ie.driver", getWebDriverBinary().getAbsolutePath());
                break;
            }
        }
    }
}
