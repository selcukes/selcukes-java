package io.github.selcukes.dp.util;

import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.logging.Logger;

public class FileHelper {
    private static Logger logger = Logger.getLogger(FileHelper.class.getName());

    private FileHelper() {

    }

    public static void setFileExecutable(String fileName) {
        try {
            final Path filepath = Paths.get(fileName);
            final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(filepath);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            Files.setPosixFilePermissions(filepath, permissions);
        } catch (Exception e) {
            throw new DriverPoolException("Unable to set WebDriver Binary file as executable :" + e);
        }
    }

    public static String getDriverBinaryFileName(String browserName) {

        return browserName + "driver-" + Platform.getPlatformArch() + "-" + Platform.getPlatform();
    }

    public static void createDirectory(File dirName) {
        boolean dirExists = dirName.exists();
        if (!dirExists) {
            logger.info("Creating directory: " + dirName.getName());
            dirExists = dirName.mkdirs();
            if (dirExists) {
                logger.info(dirName.getName()+ " directory created...");
            }
        }
    }
}
