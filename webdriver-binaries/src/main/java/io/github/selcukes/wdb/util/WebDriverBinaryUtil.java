/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.wdb.util;

import io.github.selcukes.commons.exception.WebDriverBinaryException;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.os.OsType;
import io.github.selcukes.wdb.BinaryInfo;
import io.github.selcukes.wdb.core.BinaryFactory;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.version.CacheManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class WebDriverBinaryUtil {
    private static final String WEBDRIVER = "webdriver";
    private final Logger logger = LoggerFactory.getLogger(WebDriverBinaryUtil.class);
    private final boolean strictDownload;
    private final BinaryFactory binaryFactory;
    private File binaryDownloadDirectory;

    public WebDriverBinaryUtil(BinaryFactory binaryFactory, String downloadLocation, boolean strictDownload, boolean clearBinaryCache, boolean autoCheck) {
        this.binaryFactory = binaryFactory;
        this.binaryDownloadDirectory = getBinaryDownloadDirectory(downloadLocation, clearBinaryCache);
        this.strictDownload = strictDownload;
        CacheManager.setTargetPath(binaryDownloadDirectory.getAbsolutePath());
        this.binaryFactory.browserVersion(autoCheck);
    }

    private File getBinaryDownloadDirectory(String downloadLocation, boolean clearBinaryCache) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + WEBDRIVER + File.separator);
        if (clearBinaryCache) {
            FileHelper.deleteFilesInDirectory(binaryDownloadDirectory);
        }
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

            logger.info(() -> "Downloading driver binary from: " + binaryFactory.getDownloadURL());
            FileHelper.download(binaryFactory.getDownloadURL(), binaryFactory.getCompressedBinaryFile());
            logger.info(() -> String.format("%s successfully downloaded to: %s", binaryFactory.getBinaryDriverName(), getWebDriverBinary().getParent()));

            if (binaryFactory.getCompressedBinaryType().equals(DownloaderType.JAR)) {
                FileHelper.createDirectory(new File(binaryDownloadDirectory + File.separator + binaryFactory.getBinaryDirectory()));
                try {
                    FileUtils.copyFile(binaryFactory.getCompressedBinaryFile(), getWebDriverBinary());
                    binaryFactory.getCompressedBinaryFile().deleteOnExit();
                } catch (IOException e) {
                    throw new WebDriverBinaryException(e);
                }
            } else {
                decompressBinary();
            }
            logger.info(() -> String.format("%s successfully extracted to: %s", binaryFactory.getBinaryDriverName(), getWebDriverBinary().getAbsolutePath()));
        }
        return this;
    }

    private void decompressBinary() {
        FileExtractUtil.extractFile(
                binaryFactory.getCompressedBinaryFile(),
                new File(binaryDownloadDirectory + File.separator + binaryFactory.getBinaryDirectory()),
                binaryFactory.getCompressedBinaryType());
        if (Objects.equals(binaryFactory.getBinaryEnvironment().getOSType(), OsType.LINUX)) {
            try {
                FileHelper.setFileExecutable(getWebDriverBinary().getAbsolutePath());
            } catch (Exception ignored) {
                logger.warn(
                        () -> String.format("Unable to WebDriver Binary file[%s] as executable..", getWebDriverBinary().getAbsolutePath()));
            }

        }

    }

    private String configureBinary() {
        StringBuilder binaryPropertyName = new StringBuilder();

        binaryPropertyName.append(WEBDRIVER)
                .append(".")
                .append(binaryFactory.getDriverType().getName())
                .append(".driver");

        System.setProperty(binaryPropertyName.toString(), getWebDriverBinary().getAbsolutePath());
        return binaryPropertyName.toString();
    }

}
