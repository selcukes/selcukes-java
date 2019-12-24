package io.github.selcukes.wdb.util;

import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.*;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class FileExtractUtil {

    private static final Logger logger = Logger.getLogger(FileExtractUtil.class.getName());
    private static final int BUF_SIZE = 4096;

    private FileExtractUtil() {

    }

    public static File extractFile(File source, File destination, DownloaderType compressedBinaryType) {
        FileHelper.createDirectory(destination);
        final File extractedFile = compressedBinaryType.equals(DownloaderType.ZIP) ? unZipFile(source, destination) : unTarFile(source, destination);

        final File[] directoryContents = (extractedFile != null) ? extractedFile.listFiles() : new File[0];

        if (directoryContents != null && directoryContents.length == 0) {
            throw new WebDriverBinaryException("The file unpacking failed for: " + source.getAbsolutePath());
        }
        return extractedFile;
    }

    private static File unZipFile(File source, File destination) {
        File unZippedFile = null;

        try (final ZipInputStream inputStream = new ZipInputStream(new FileInputStream(source.toString()))) {
            ZipEntry zipEntry = inputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                logger.severe(() -> String.format("Unzipping {%s} (size: {%d} KB, compressed size: {%d} KB)",
                    fileName, size, compressedSize));
                unZippedFile = new File(destination.getAbsolutePath() + File.separator + fileName);
                processFile(inputStream, unZippedFile);
                zipEntry = inputStream.getNextEntry();
            }
            inputStream.closeEntry();
        } catch (IOException ex) {
            throw new WebDriverBinaryException(ex);
        }
        return unZippedFile;
    }

    private static File unTarFile(File source, File destination) {
        String outputFile = getFileName(source, destination.getAbsolutePath());

        File tarFile = deCompressGZipFile(source, new File(outputFile));

        try (final TarArchiveInputStream inputStream = new TarArchiveInputStream(new FileInputStream(tarFile))) {
            TarArchiveEntry tarEntry = inputStream.getNextTarEntry();

            while (tarEntry != null) {
                String fileName = tarEntry.getName();
                long size = tarEntry.getSize();
                long compressedSize = tarEntry.getSize();
                logger.severe(() -> String.format("Uncompressing {%s} (size: {%d} KB, compressed size: {%d} KB)",
                    fileName, size, compressedSize));
                tarFile = new File(destination.getAbsolutePath() + File.separator + fileName);

                processFile(inputStream, tarFile);
                tarEntry = inputStream.getNextTarEntry();
            }
            inputStream.getCurrentEntry();
        } catch (IOException ex) {
            throw new WebDriverBinaryException(ex);
        }
        return tarFile;
    }

    private static File deCompressGZipFile(File gZippedFile, File tarFile) {
        try (FileInputStream fis = new FileInputStream(gZippedFile);
             GZIPInputStream gZIPInputStream = new GZIPInputStream(fis)) {
            processFile(gZIPInputStream, tarFile);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return tarFile;

    }

    private static void processFile(InputStream inputStream, File outDir) {

        byte[] buffer = new byte[BUF_SIZE];

        try (FileOutputStream fos = new FileOutputStream(outDir)) {
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

        } catch (IOException ex) {
            logger.severe("Unable to uncompress File: " + ex.getMessage());
        }

    }

    private static String getFileName(File inputFile, String outputFolder) {
        return outputFolder + File.separator +
            inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
    }

}
