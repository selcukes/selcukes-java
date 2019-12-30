package io.github.selcukes.wdb.util;

import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;


public class FileHelper {
    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    private FileHelper() {

    }

    public static void setFileExecutable(String fileName) {
        try {
            final Path filepath = Paths.get(fileName);
            final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(filepath);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            Files.setPosixFilePermissions(filepath, permissions);
        } catch (Exception e) {
            logger.error(e::getMessage);
            throw new WebDriverBinaryException("Unable to set WebDriver Binary file as executable :" + e);
        }
    }

    public static void createDirectory(File dirName) {
        boolean dirExists = dirName.exists();
        if (!dirExists) {
            logger.debug(()->"Creating directory: " + dirName.getName());
            dirExists = dirName.mkdirs();
            if (dirExists) {
                logger.debug(()->dirName.getName() + " directory created...");
            }
        }
    }
}
