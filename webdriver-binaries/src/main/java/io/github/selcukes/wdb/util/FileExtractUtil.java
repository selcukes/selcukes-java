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
import io.github.selcukes.wdb.enums.DownloaderType;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;

public final class FileExtractUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileExtractUtil.class);

    private FileExtractUtil() {

    }

    public static File extractFile(File source, File destination, DownloaderType compressedBinaryType) {
        logger.info(() -> String.format("Extracting binary from compressed file %s", source));

        if (compressedBinaryType.equals(DownloaderType.ZIP))
            unZipFile(source, destination);
        else
            unTarFile(source, destination);

        final File[] directoryContents = (destination != null) ? destination.listFiles() : new File[0];

        if (directoryContents != null && directoryContents.length == 0) {
            throw new WebDriverBinaryException("The file unpacking failed for: " + source.getAbsolutePath());
        }
        logger.debug(() -> "Deleting compressed file:" + source.getAbsolutePath());
        source.deleteOnExit();

        return destination;
    }

    private static void unZipFile(File source, File destination) {

        try (FileInputStream fis = new FileInputStream(source);
             ZipArchiveInputStream zis = new ZipArchiveInputStream(fis)) {
            ZipArchiveEntry entry;
            while ((entry = zis.getNextZipEntry()) != null) {
                uncompress(zis, destination, entry);
            }
        } catch (IOException e) {
            throw new WebDriverBinaryException(e);
        }

    }

    private static void unTarFile(File source, File destination) {
        try (FileInputStream fis = new FileInputStream(source);
             GZIPInputStream gZIPInputStream = new GZIPInputStream(fis);
             final TarArchiveInputStream tis = new TarArchiveInputStream(gZIPInputStream)) {
            TarArchiveEntry entry;

            while ((entry = tis.getNextTarEntry()) != null) {
                uncompress(tis, destination, entry);
            }
        } catch (IOException ex) {
            throw new WebDriverBinaryException(ex);
        }

    }

    private static void uncompress(InputStream inputStream, File destination, ArchiveEntry entry) throws IOException {
        String fileName = entry.getName();
        long size = entry.getSize();
        long compressedSize = entry.getSize();
        logger.info(() -> String.format("Uncompressing {%s} (size: {%d} KB, compressed size: {%d} KB)",
            fileName, size, compressedSize));
        File entryDestination = new File(destination.getAbsolutePath() + File.separator + fileName);
        if (entry.isDirectory()) {
            FileHelper.createDirectory(entryDestination);
        } else {
            FileHelper.createDirectory(entryDestination.getParentFile());
            FileOutputStream fos = new FileOutputStream(entryDestination);
            IOUtils.copy(inputStream, fos);
            fos.close();
        }
    }
}