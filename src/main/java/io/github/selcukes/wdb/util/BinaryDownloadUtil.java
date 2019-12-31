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

import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public final class BinaryDownloadUtil {


    private static final Logger logger = LoggerFactory.getLogger(BinaryDownloadUtil.class);

    private BinaryDownloadUtil() {

    }

    public static void downloadBinary(URL downloadURL, File downloadTo) {
        download(downloadURL, downloadTo, false);
    }

    public static String downloadAndReadFile(URL downloadURL) {
        File destinationFile = TempFileUtil.createTempFileAndDeleteOnExit();

        download(downloadURL, destinationFile, true);

        if (destinationFile.exists()) {
            try {
                return FileUtils.readFileToString(destinationFile, Charset.defaultCharset()).trim();
            } catch (IOException e) {
                throw new WebDriverBinaryException(e);
            }
        }
        throw new WebDriverBinaryException("Unable to download file from: " + getAbsoluteURL(downloadURL));
    }

    private static void download(URL downloadURL, File downloadTo, boolean silentDownload) {
        try {

            if (!silentDownload) {
                logger.info(() -> "Downloading driver binary from: " + getAbsoluteURL(downloadURL));
            }

            FileUtils.copyURLToFile(downloadURL, downloadTo);

        } catch (IOException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    private static String getAbsoluteURL(URL url) {
        return url.getProtocol() + "://" + url.getHost() + url.getPath();
    }
}