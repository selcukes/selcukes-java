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
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.wdb.enums.DownloaderType;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {

            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {
                boolean isDirectory = false;
                if (entry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(entry, destination.toPath());

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                }
                entry = zis.getNextEntry();
            }
            zis.closeEntry();

        } catch (IOException e) {
            throw new WebDriverBinaryException(e);
        }
        return destination;
    }

    private static File unTarFile(File source, File destination) {
        try (FileInputStream fis = new FileInputStream(source);
             GZIPInputStream gZIPInputStream = new GZIPInputStream(fis);
             final TarArchiveInputStream inputStream = new TarArchiveInputStream(gZIPInputStream)) {
            TarArchiveEntry entry;

            while ((entry = inputStream.getNextTarEntry()) != null) {
                Path newPath = zipSlipProtect(entry, destination.toPath());
                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {

                    Path parent = newPath.getParent();
                    if (parent != null) {
                        if (Files.notExists(parent)) {
                            Files.createDirectories(parent);
                        }
                    }
                    Files.copy(inputStream, newPath, StandardCopyOption.REPLACE_EXISTING);

                }
            }
        } catch (IOException ex) {
            throw new WebDriverBinaryException(ex);
        }
        return destination;
    }

    public static Path zipSlipProtect(ZipEntry entry, Path targetDir)
        throws IOException {

        Path targetDirResolved = targetDir.resolve(entry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + entry.getName());
        }

        return normalizePath;
    }

    public static Path zipSlipProtect(ArchiveEntry entry, Path targetDir)
        throws IOException {

        Path targetDirResolved = targetDir.resolve(entry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + entry.getName());
        }

        return normalizePath;
    }
}