package io.github.selcukes.wdb.util;

import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
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

        final File extractedFile = compressedBinaryType.equals(DownloaderType.ZIP) ? unZipFile(source, destination) : unTarFile(source, destination);

        final File[] directoryContents = (extractedFile != null) ? extractedFile.listFiles() : new File[0];

        if (directoryContents != null && directoryContents.length == 0) {
            throw new WebDriverBinaryException("The file unpacking failed for: " + source.getAbsolutePath());
        }
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
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    copyFile(inputStream, entryDestination);
                }
            }
        } catch (IOException e) {
            logger.error(e::getMessage);
            throw new WebDriverBinaryException(e);
        }
        return entryDestination;
    }

    private static File unTarFile(File source, File destination) {
        String outputFile = getFileName(source, destination.getAbsolutePath());

        File entryDestination = deCompressGZipFile(source, new File(outputFile));

        try (final TarArchiveInputStream inputStream = new TarArchiveInputStream(new FileInputStream(entryDestination))) {
            TarArchiveEntry tarEntry;

            while ((tarEntry = inputStream.getNextTarEntry()) != null) {
                String fileName = tarEntry.getName();
                long size = tarEntry.getSize();
                long compressedSize = tarEntry.getSize();
                logger.info(() -> String.format("Uncompressing {%s} (size: {%d} KB, compressed size: {%d} KB)",
                    fileName, size, compressedSize));
                entryDestination = new File(destination.getAbsolutePath() + File.separator + fileName);
                if (tarEntry.isDirectory()) {
                    if (!entryDestination.exists()) {
                        FileHelper.createDirectory(entryDestination);
                    }
                } else {
                    FileHelper.createDirectory(entryDestination.getParentFile());
                    copyFile(inputStream, entryDestination);
                }
            }
        } catch (IOException e) {
            logger.error(e::getMessage);
            throw new WebDriverBinaryException(e);
        }
        return entryDestination;
    }

    private static File deCompressGZipFile(File gZippedFile, File tarFile) {
        try (FileInputStream fis = new FileInputStream(gZippedFile);
             GZIPInputStream gZIPInputStream = new GZIPInputStream(fis)) {
            copyFile(gZIPInputStream, tarFile);
        } catch (IOException e) {
            logger.error(e::getMessage);
        }
        return tarFile;

    }

    private static void copyFile(InputStream inputStream, File outDir) {

        try (FileOutputStream outputStream = new FileOutputStream(outDir)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            logger.error(() -> "Unable to Copy File: " + e.getMessage());
            throw new WebDriverBinaryException(e);
        }

    }

    private static String getFileName(File inputFile, String outputFolder) {
        return outputFolder + File.separator +
            inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
    }

}
