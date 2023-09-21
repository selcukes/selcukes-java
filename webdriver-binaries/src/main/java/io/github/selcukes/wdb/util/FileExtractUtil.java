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
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.zip.GZIPInputStream;

public final class FileExtractUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileExtractUtil.class);

    private FileExtractUtil() {

    }

    public static void extractFile(Path source, Path destination, DownloaderType compressedBinaryType) {
        logger.info(() -> String.format("Extracting binary from compressed file %s", source));
        final var extractedFile = compressedBinaryType.equals(DownloaderType.ZIP) ? unZipFile(source, destination)
                : unTarFile(source, destination);

        try (var filesInDirectory = Files.list(destination)) {
            if (filesInDirectory.findAny().isEmpty() || extractedFile == null) {
                throw new WebDriverBinaryException("No files were extracted to: " + destination);
            }
        } catch (NoSuchFileException e) {
            throw new WebDriverBinaryException(
                "The destination folder does not exist: " + destination, e);
        } catch (IOException e) {
            throw new WebDriverBinaryException(
                "Error occurred while checking the destination folder: " + destination, e);
        }
        logger.debug(() -> "Deleting compressed file:" + source.toAbsolutePath());
        source.toFile().deleteOnExit();
    }

    private static Path unZipFile(Path source, Path destination) {
        Path entryDestination = null;
        try (var fis = Files.newInputStream(source, StandardOpenOption.READ);
                var zis = new ZipArchiveInputStream(fis)) {
            ZipArchiveEntry entry;
            while ((entry = zis.getNextZipEntry()) != null) {
                if (!entry.getName().toUpperCase().contains("LICENSE")) {
                    entryDestination = uncompress(zis, destination, entry);
                }
            }
        } catch (IOException e) {
            throw new WebDriverBinaryException(e);
        }
        return entryDestination;
    }

    private static Path unTarFile(Path source, Path destination) {
        Path entryDestination = null;
        try (var fis = Files.newInputStream(source, StandardOpenOption.READ);
                var gZIPInputStream = new GZIPInputStream(fis);
                final var tis = new TarArchiveInputStream(gZIPInputStream)) {
            TarArchiveEntry entry;
            while ((entry = tis.getNextTarEntry()) != null) {
                if (!entry.getName().toUpperCase().contains("LICENSE")) {
                    entryDestination = uncompress(tis, destination, entry);
                }

            }
        } catch (IOException ex) {
            throw new WebDriverBinaryException(ex);
        }
        return entryDestination;
    }

    private static Path uncompress(InputStream inputStream, Path destination, ArchiveEntry entry) throws IOException {
        String fileName = entry.getName();
        var entryDestination = destination.resolve(fileName);
        logger.info(() -> String.format("Uncompressing {%s} (size: {%d} KB, compressed size: {%d} KB)",
            fileName, entry.getSize(), entry.getSize()));

        if (entry.isDirectory()) {
            Files.createDirectories(entryDestination);
        } else {
            Files.createDirectories(entryDestination.getParent());
            Files.copy(inputStream, entryDestination, StandardCopyOption.REPLACE_EXISTING);
            return entryDestination;
        }
        return entryDestination;
    }
}
