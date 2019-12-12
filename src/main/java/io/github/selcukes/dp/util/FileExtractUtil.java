package io.github.selcukes.dp.util;

import io.github.selcukes.dp.enums.DownloaderType;
import io.github.selcukes.dp.exception.DriverPoolException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The type File extract util.
 */
public final class FileExtractUtil {

    private static final Logger logger=Logger.getLogger(FileExtractUtil.class.getName());
    private static final int BUF_SIZE = 4096;

    /**
     * Extract file file.
     *
     * @param source               the source
     * @param destination          the destination
     * @param compressedBinaryType the compressed binary type
     * @return the file
     */
    public static File extractFile(File source, File destination, DownloaderType compressedBinaryType) {
        createDestinationDirectory(destination);

        final File extractedFile = compressedBinaryType.equals(DownloaderType.ZIP) ? unZipFile(source, destination) : unTarFile(source, destination);

        final File[] directoryContents = extractedFile.listFiles();

        if (directoryContents != null && directoryContents.length == 0) {
            throw new DriverPoolException("The file unpacking failed for: " + source.getAbsolutePath());
        }
        return extractedFile;
    }

    private static File unZipFile(File source, File destination) {
        File unZippedFile = null;

        try {
            byte[] buffer = new byte[BUF_SIZE];

            final ZipInputStream inputStream = new ZipInputStream(new FileInputStream(source.toString()));

            ZipEntry zipEntry = inputStream.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                logger.severe(()->String.format("Unzipping {%s} (size: {%d} KB, compressed size: {%d} KB)",
                        fileName, size, compressedSize));
                unZippedFile = new File(destination.getAbsolutePath() + File.separator + fileName);

                FileOutputStream fos = new FileOutputStream(unZippedFile);
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = inputStream.getNextEntry();
            }
            inputStream.closeEntry();
            inputStream.close();
        } catch (IOException ex) {
            throw new DriverPoolException(ex);
        }
        return unZippedFile;
    }

    private static File unTarFile(File source, File destination) {
        File unTaredFile = null;

        try {
            byte[] buffer = new byte[BUF_SIZE];

            final TarArchiveInputStream inputStream = new TarArchiveInputStream(new FileInputStream(source.toString()));

            TarArchiveEntry tarEntry = inputStream.getNextTarEntry();

            while (tarEntry != null) {
                String fileName = tarEntry.getName();
                long size = tarEntry.getSize();
                long compressedSize = tarEntry.getSize();
                logger.severe(()->String.format("Untarring {%s} (size: {%d} KB, compressed size: {%d} KB)",
                        fileName, size, compressedSize));
                unTaredFile = new File(destination.getAbsolutePath() + File.separator + fileName);

                FileOutputStream fos = new FileOutputStream(unTaredFile);
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                tarEntry = inputStream.getNextTarEntry();
            }
            inputStream.getCurrentEntry();
            inputStream.close();
        } catch (IOException ex) {
            throw new DriverPoolException(ex);
        }
        return unTaredFile;
    }

    private static void createDestinationDirectory(File destination) {
        if (!destination.exists()) {
            destination.mkdir();
        }
    }
}
