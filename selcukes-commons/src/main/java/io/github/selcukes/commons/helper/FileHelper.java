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

package io.github.selcukes.commons.helper;

import io.github.selcukes.commons.exception.ConfigurationException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;

/**
 * The type File helper.
 */
@UtilityClass
public class FileHelper {

    /**
     * The Resource separator.
     */
    public final String RESOURCE_SEPARATOR = "/";
    private final String SUPPORT_FOLDER = "support";

    /**
     * Drivers folder string.
     *
     * @param  path the path
     * @return      the string
     */
    public String driversFolder(final String path) {
        File file = new File(path);
        for (String item : Objects.requireNonNull(file.list())) {

            if (SUPPORT_FOLDER.equals(item)) {
                return file.getAbsolutePath() + RESOURCE_SEPARATOR + SUPPORT_FOLDER + RESOURCE_SEPARATOR;
            }
        }
        return driversFolder(file.getParent());
    }

    /**
     * > It takes a file path as a parameter and sets the file as executable
     *
     * @param filePath The path to the file you want to make executable.
     */
    public void setFileExecutable(final String filePath) {
        try {
            var path = Paths.get(filePath);
            final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            Files.setPosixFilePermissions(path, permissions);
        } catch (Exception e) {
            throw new ConfigurationException("Unable to set file as executable..");
        }
    }

    /**
     * If the file separator is a backslash, then replace all forward slashes
     * with backslashes.
     *
     * @param  filePath The file path to be converted.
     * @return          The file path with the correct file separator for the
     *                  system.
     */
    public String systemFilePath(final String filePath) {
        String fileSeparator = System.getProperty("file.separator");

        if (!fileSeparator.isEmpty() && "\\".equals(fileSeparator)) {
            int beginIndex = 0;

            if (filePath.startsWith(RESOURCE_SEPARATOR)) {
                beginIndex = 1;
            }

            return filePath.substring(beginIndex).replace(RESOURCE_SEPARATOR, "\\");
        }

        return filePath;
    }

    /**
     * If the directory exists, check if it is a directory. If it is not a
     * directory, throw an exception. If the directory does not exist, create
     * it. If the directory is not created, throw an exception
     *
     * @param directory The directory to create, including any necessary but
     *                  nonexistent parent directories.
     */
    public void createDirectory(final File directory) {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                throw new ConfigurationException(
                    "File " + directory + " exists and is not a directory. Unable to create directory.");
            }
        } else if (!directory.mkdirs() && !directory.isDirectory()) {
            throw new ConfigurationException("Unable to create directory " + directory);
        }
    }

    /**
     * It creates a directory if it doesn't exist
     *
     * @param  directory The directory to create.
     * @return           Path
     */
    public Path createDirectory(final String directory) {
        try {
            Path path = Paths.get(directory);
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new ConfigurationException("Unable to create directory : " + directory, e);
        }
    }

    /**
     * It deletes all the files in the directory
     *
     * @param dirName The directory to delete.
     */
    public void deleteFilesInDirectory(final File dirName) {
        try {
            FileUtils.cleanDirectory(dirName);
        } catch (IOException e) {
            throw new ConfigurationException("'" + dirName + "' is not a directory or a file to delete.", e);
        }
    }

    /**
     * If the file exists, delete it.
     *
     * @param fileName The file to be deleted.
     */
    public void deleteFile(final File fileName) {
        if (fileName.exists()) {
            FileUtils.deleteQuietly(fileName);
        }
    }

    /**
     * RenameFile() renames a file from one name to another.
     *
     * @param from The file to be renamed
     * @param to   The file to rename to.
     */
    public void renameFile(final File from, final File to) {
        if (!from.renameTo(to)) {
            throw new ConfigurationException("Failed to rename " + from + " to " + to);
        }
    }

    /**
     * It creates a temporary directory in the default temporary-file directory,
     * using the given prefix and suffix to generate its name
     *
     * @return A Path object
     */
    public Path createTempDirectory() {
        try {
            return Files.createTempDirectory(null);
        } catch (IOException e) {
            throw new ConfigurationException("Unable to create temp directory : ", e);
        }
    }

    /**
     * It creates a temporary file in the default temporary-file directory,
     * using the given prefix and suffix to generate its name
     *
     * @return A file object
     */
    public File createTempFile() {
        try {
            File tempFile = File.createTempFile("WDB", ".temp");
            tempFile.deleteOnExit();
            return tempFile;
        } catch (IOException e) {
            throw new ConfigurationException("Unable to create temp file : ", e);
        }
    }

    /**
     * It converts a file to a byte array.
     *
     * @param filePath The path of the file to be converted to a byte array.
     */
    public byte[] toByteArray(final String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new ConfigurationException("Unable to convert file to ByteArray  : ", e);
        }
    }

    /**
     * Create a temporary file, get its parent directory, and return it.
     *
     * @return The parent directory of the temporary file.
     */
    public String getTempDir() {
        return createTempFile().getParent();
    }

    /**
     * It loads a file from the resources folder
     *
     * @param  file The file to load.
     * @return      A File object
     */
    public File loadResource(final String file) {
        return new File(Objects.requireNonNull(FileHelper.class.getClassLoader().getResource(file)).getFile());
    }

    /**
     * Load a file from the classpath using the current thread's classloader.
     *
     * @param  file The file to load.
     * @return      A File object
     */
    public File loadThreadResource(final String file) {
        return new File(
            Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(file)).getFile());
    }

    /**
     * Load a resource from the classpath using the current thread's
     * classloader.
     *
     * @param  file The file to load.
     * @return      A stream of the file.
     */
    public InputStream loadThreadResourceAsStream(final String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }

    /**
     * Load a resource from the classpath as an InputStream.
     *
     * @param  file The file to load.
     * @return      A stream of bytes from the file.
     */
    public InputStream loadResourceAsStream(final String file) {
        return FileHelper.class.getClassLoader().getResourceAsStream(file);
    }

    /**
     * Load a resource from a jar file.
     *
     * @param  file The file to load.
     * @return      A stream of the file.
     */
    public InputStream loadResourceFromJar(final String file) {
        return FileHelper.class.getResourceAsStream("/" + file);
    }

    /**
     * Gets watermark file.
     *
     * @return the watermark file
     */
    public File getWaterMarkFile() {
        String waterMark = "target/selcukes-watermark.png";
        File waterMarkFile = new File(waterMark);
        if (!waterMarkFile.exists()) {
            download(Objects.requireNonNull(FileHelper.class.getClassLoader().getResource("selcukes-watermark.png")),
                waterMarkFile);
        }
        return waterMarkFile;
    }

    /**
     * It takes a base64 encoded string and writes it to a file
     *
     * @param  fileContent The base64 encoded file content
     * @param  filePath    The path where the file will be created.
     * @return             A File object
     */
    @SneakyThrows
    public File createFile(final String fileContent, final String filePath) {
        byte[] decodedFile = Base64.getDecoder()
                .decode(fileContent.getBytes(StandardCharsets.UTF_8));
        Path destinationFile = Paths.get(filePath);
        Files.write(destinationFile, decodedFile);
        return destinationFile.toFile();
    }

    /**
     * > It downloads a file from a given URL and saves it to a given
     * destination
     *
     * @param source      The URL of the file to download.
     * @param destination The file to download to.
     */
    public void download(final URL source, final File destination) {
        if (destination.exists()) {
            return;
        }
        try (
                var urlStream = source.openStream();
                var readableByteChannel = Channels.newChannel(urlStream);
                var fileOutputStream = new FileOutputStream(destination)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            throw new ConfigurationException(String.format("Unable to download a file from URL [%s]", source));
        }
    }

    /**
     * "Download the file at the given URL to the user's home directory, and
     * return the path to the downloaded file."
     * <p>
     * The first line of the function is a call to the `Arrays.stream` method.
     * This method takes an array of strings and returns a stream of strings.
     * The stream is then filtered to only include the last element of the
     * array. The `reduce` method is then called on the stream, which takes two
     * parameters: a function that takes two strings and returns a string, and a
     * default value. The function is called on each element of the stream, and
     * the result of the function is passed to the next call of the function.
     * The default value is used as the first parameter to the first call of the
     * function. The `reduce` method returns the result of the last call of the
     * function
     *
     * @param  url The URL of the file to download.
     * @return     The path to the file.
     */
    @SneakyThrows
    public String downloadToUsersFolder(final String url) {
        var fileName = Arrays.stream(url.split("/")).reduce((first, second) -> second).orElse(null);
        var userHomeDir = System.getProperty("user.home");
        var file = Paths.get(userHomeDir, fileName).toFile();
        if (file.exists()) {
            return file.getPath();
        }
        download(new URL(url), file);
        return file.getPath();
    }

    /**
     * > It loads a file from the classpath and returns its content as a string
     *
     * @param  filePath The path to the file you want to read.
     * @return          The content of the file.
     */
    public String readContent(final String filePath) {
        try {
            return new String(loadResourceFromJar(filePath).readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new ConfigurationException(String.format("Cannot load [%s] from classpath", filePath));
        }
    }
}
