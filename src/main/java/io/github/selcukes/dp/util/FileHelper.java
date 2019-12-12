package io.github.selcukes.dp.util;

import io.github.selcukes.dp.exception.DriverPoolException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * The type File helper.
 */
public class FileHelper {
    /**
     * Sets file executable.
     *
     * @param fileName the file name
     */
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

    /**
     * Gets driver binary file name.
     *
     * @param browserName the browser name
     * @return the driver binary file name
     */
    public static String getDriverBinaryFileName(String browserName) {
        String arch = System.getProperty("os.arch").contains("64") ? "64" : "32";
        String os = System.getProperty("os.name").toLowerCase().contains("win") ? "win.exe" : "linux";
        return browserName + "driver-" + arch + "-" + os;
    }

    /**
     * Create directory.
     *
     * @param dirName the dir name
     * @throws IOException the io exception
     */
    public static void createDirectory(File dirName) throws IOException {
        if (!dirName.exists()) {
            dirName.mkdirs();
        } else if (!dirName.isDirectory()) {
            throw new IOException("\"" + dirName + "\" is not a directory or a file.");
        }
    }
}
