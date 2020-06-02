/*
 *
 *  * Copyright (c) Ramesh Babu Prudhvi.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.selcukes.wdb.util;

import io.github.selcukes.core.exception.WebDriverBinaryException;
import io.github.selcukes.core.helper.FileHelper;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.BinaryInfo;
import io.github.selcukes.wdb.core.BinaryFactory;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.OSType;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class WebDriverBinaryUtil {
    private final Logger logger = LoggerFactory.getLogger(WebDriverBinaryUtil.class);
    private File binaryDownloadDirectory;
    private final boolean strictDownload;
    private final BinaryFactory binaryFactory;
    private static final String WEBDRIVER = "webdriver";


    public WebDriverBinaryUtil(BinaryFactory binaryFactory, String downloadLocation, boolean strictDownload) {
        this.binaryFactory = binaryFactory;
        this.binaryDownloadDirectory = getBinaryDownloadDirectory(downloadLocation);
        this.strictDownload = strictDownload;
    }

    private File getBinaryDownloadDirectory(String downloadLocation) {
        binaryDownloadDirectory = new File(downloadLocation + File.separator + WEBDRIVER + File.separator);
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
                    binaryFactory.getCompressedBinaryFile().deleteOnExit();
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
        if (Objects.equals(binaryFactory.getBinaryEnvironment().getOSType(), OSType.LINUX))
            FileHelper.setFileExecutable(decompressedBinary.getAbsolutePath());
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
