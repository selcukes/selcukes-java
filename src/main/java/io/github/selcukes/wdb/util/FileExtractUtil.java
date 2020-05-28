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
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.enums.DownloaderType;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class FileExtractUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileExtractUtil.class);

    private FileExtractUtil() {

    }

    public static File extractFile(File source, File destination, DownloaderType compressedBinaryType) {
        logger.info(() -> String.format("Extracting binary from compressed file %s", source));
        final File extractedFile = compressedBinaryType.equals(DownloaderType.ZIP) ? unZipFile(source, destination) : unTarFile(source, destination);

        final File[] directoryContents = (extractedFile != null) ? extractedFile.listFiles() : new File[0];

        if (directoryContents != null && directoryContents.length == 0) {
            throw new WebDriverBinaryException("The file unpacking failed for: " + source.getAbsolutePath());
        }
        logger.debug(() -> "Deleting compressed file:" + source.getAbsolutePath());
        source.deleteOnExit();
        return extractedFile;
    }

    private static File unZipFile(File source, File destination) {
        File entryDestination = null;
        try (ZipFile zipFile = new ZipFile(source)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String fileName = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                logger.info(() -> String.format("Unzipping {%s} (size: {%d} KB, compressed size: {%d} KB)",
                    fileName, size, compressedSize));
                entryDestination = new File(destination.getAbsolutePath() + File.separator + fileName);
                if (zipEntry.isDirectory()) {
                    FileHelper.createDirectory(entryDestination);
                } else {
                    FileHelper.createDirectory(entryDestination.getParentFile());
                    InputStream in = zipFile.getInputStream(zipEntry);
                    OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(in, out);
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new WebDriverBinaryException(e);
        }
        return entryDestination;
    }

    private static File unTarFile(File source, File destination) {
        File entryDestination = null;
        try (FileInputStream fis = new FileInputStream(source);
             GZIPInputStream gZIPInputStream = new GZIPInputStream(fis);
             final TarArchiveInputStream inputStream = new TarArchiveInputStream(gZIPInputStream)) {
            TarArchiveEntry tarEntry;

            while ((tarEntry = inputStream.getNextTarEntry()) != null) {
                String fileName = tarEntry.getName();
                long size = tarEntry.getSize();
                long compressedSize = tarEntry.getSize();
                logger.info(() -> String.format("Decompressing {%s} (size: {%d} KB, compressed size: {%d} KB)",
                    fileName, size, compressedSize));
                entryDestination = new File(destination.getAbsolutePath() + File.separator + fileName);
                if (tarEntry.isDirectory()) {
                    if (!entryDestination.exists()) {
                        FileHelper.createDirectory(entryDestination);
                    }
                } else {
                    FileHelper.createDirectory(entryDestination.getParentFile());
                    FileOutputStream fos = new FileOutputStream(entryDestination);
                    IOUtils.copy(inputStream, fos);
                    fos.close();
                }
            }
        } catch (IOException ex) {
            throw new WebDriverBinaryException(ex);
        }
        return entryDestination;
    }
}