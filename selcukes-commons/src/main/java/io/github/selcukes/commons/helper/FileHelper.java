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

import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
    private final String SUPPORT_FOLDER = "support";

    /**
     * Drivers folder string.
     *
     * @param path the path
     * @return the string
     */
    public String driversFolder(String path) {
        File file = new File(path);
        for (String item : Objects.requireNonNull(file.list())) {

            if (SUPPORT_FOLDER.equals(item)) {
                return file.getAbsolutePath() + RESOURCE_SEPARATOR + SUPPORT_FOLDER + RESOURCE_SEPARATOR;
            }
        }
        return driversFolder(file.getParent());
    }

    /**
     * Sets file executable.
     *
     * @param filePath the file path
     */
    public void setFileExecutable(String filePath) {
        try {
            final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(Paths.get(filePath));
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            Files.setPosixFilePermissions(Paths.get(filePath), permissions);
        } catch (Exception e) {
            throw new SelcukesException("Unable to set file as executable..");
        }
    }

    /**
     * System file path string.
     *
     * @param filePath the file path
     * @return the string
     */
    public String systemFilePath(String filePath) {
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
     * Create directory.
     *
     * @param directory the directory
     */
    public void createDirectory(File directory) {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                throw new SelcukesException("File " + directory + " exists and is not a directory. Unable to create directory.");
            }
        } else if (!directory.mkdirs() && !directory.isDirectory()) {
            throw new SelcukesException("Unable to create directory " + directory);
        }
    }

    /**
     * Create directory path.
     *
     * @param directory the directory
     * @return the path
     */
    public Path createDirectory(String directory) {
        try {
            Path path = Paths.get(directory);
            return Files.createDirectories(path);
        } catch (IOException e) {
            throw new SelcukesException("Unable to create directory : " + directory, e);
        }
    }

    /**
     * Delete files in directory.
     *
     * @param dirName the dir name
     */
    public void deleteFilesInDirectory(File dirName) {
        try {
            FileUtils.cleanDirectory(dirName);
        } catch (IOException e) {
            LOGGER.error(e::getMessage);
            throw new SelcukesException("'" + dirName + "' is not a directory or a file to delete.", e);
        }
    }

    /**
     * Delete file.
     *
     * @param fileName the file name
     */
    public void deleteFile(File fileName) {
        if (fileName.exists())
            FileUtils.deleteQuietly(fileName);
    }

    /**
     * Rename file.
     *
     * @param from the from
     * @param to   the to
     */
    public void renameFile(File from, File to) {
        if (!from.renameTo(to)) {
            throw new SelcukesException("Failed to rename " + from + " to " + to);
        }
    }

    /**
     * Create temp directory path.
     *
     * @return the path
     */
    public Path createTempDirectory() {
        try {
            return Files.createTempDirectory(null);
        } catch (IOException e) {
            throw new SelcukesException("Unable to create temp directory : ", e);
        }
    }

    /**
     * Create Temp file.
     *
     * @return the file
     */
    public File createTempFile() {
        try {
            File tempFile = File.createTempFile("WDB", ".temp");
            tempFile.deleteOnExit();
            return tempFile;
        } catch (IOException e) {
            throw new SelcukesException("Unable to create temp file : ", e);
        }
    }

    /**
     * To byte array byte[].
     *
     * @param filePath the file path
     * @return the byte[]
     */
    public byte[] toByteArray(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new SelcukesException("Unable to convert file to ByteArray  : ", e);
        }
    }

    /**
     * Gets temp dir.
     *
     * @return the temp dir
     */
    public String getTempDir() {
        return createTempFile().getParent();
    }

    /**
     * Load resource file.
     *
     * @param file the file
     * @return the file
     */
    public File loadResource(String file) {
        return new File(Objects.requireNonNull(FileHelper.class.getClassLoader().getResource(file)).getFile());
    }

    /**
     * Load thread resource file.
     *
     * @param file the file
     * @return the file
     */
    public File loadThreadResource(String file) {
        return new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(file)).getFile());
    }

    /**
     * Load thread resource as stream input stream.
     *
     * @param file the file
     * @return the input stream
     */
    public InputStream loadThreadResourceAsStream(String file) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
    }

    /**
     * Load resource as stream input stream.
     *
     * @param file the file
     * @return the input stream
     */
    public InputStream loadResourceAsStream(String file) {
        return FileHelper.class.getClassLoader().getResourceAsStream(file);
    }

    /**
     * Load resource from jar input stream.
     *
     * @param file the file
     * @return the input stream
     */
    public InputStream loadResourceFromJar(String file) {
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
        if (!waterMarkFile.exists())
            download(Objects.requireNonNull(FileHelper.class.
                getClassLoader().getResource("selcukes-watermark.png")), waterMarkFile);
        return waterMarkFile;
    }

    /**
     * Create new file from String fileContent.
     *
     * @param fileContent the file content
     * @param filePath    the file path
     * @return the file
     */
    @SneakyThrows
    public File createFile(String fileContent, String filePath) {
        byte[] decodedFile = Base64.getDecoder()
            .decode(fileContent.getBytes(StandardCharsets.UTF_8));
        Path destinationFile = Paths.get(filePath);
        Files.write(destinationFile, decodedFile);
        return destinationFile.toFile();
    }

    /**
     * Download a file from url.
     *
     * @param source      the source
     * @param destination the destination
     */
    public void download(URL source, File destination) {
        if (destination.exists())
            return;
        try (
            var urlStream = source.openStream();
            var readableByteChannel = Channels.newChannel(urlStream);
            var fileOutputStream = new FileOutputStream(destination);
        ) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            throw new SelcukesException(String.format("Unable to download a file from URL [%s]", source));
        }
    }

    @SneakyThrows
    public String downloadToUsersFolder(String url) {
        var fileName = Arrays.stream(url.split("/")).reduce((first, second) -> second).orElse(null);
        var userHomeDir = System.getProperty("user.home");
        var file = Paths.get(userHomeDir, fileName).toFile();
        if (file.exists())
            return file.getPath();
        download(new URL(url), file);
        return file.getPath();
    }
}

