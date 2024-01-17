/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.collections;

import io.github.selcukes.collections.exception.DataStreamException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@UtilityClass
public class Resources {
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String TEST_RESOURCES = "src/test/resources";

    /**
     * Returns the absolute path of a file given a file path. If the file path
     * is absolute, the same path is returned. If the file path is relative, the
     * absolute path is computed relative to the user's current working
     * directory.
     *
     * @param  filePath The path to the file.
     * @return          A Path object representing the absolute path of the
     *                  file.
     */
    public Path of(final String filePath) {
        var path = Path.of(filePath);
        return path.isAbsolute() ? path : Path.of(USER_DIR).resolve(filePath);
    }

    /**
     * This function checks whether the given folder path is a directory using
     * the Files.isDirectory method. If the path does not refer to a directory,
     * the function throws a DataMapperException with an error message
     * indicating that the path is not a directory.
     *
     * @param  folder The folder to check.
     * @return        A Path object.
     */
    public Path isDirectory(final String folder) {
        var folderPath = of(folder);
        if (!Files.isDirectory(folderPath)) {
            throw new DataStreamException(format("%s is not a directory.", folder));
        }
        return folderPath;
    }

    /**
     * It creates a new file at the given path and returns the name of the file
     *
     * @param  filePath The path to the file to be created.
     * @return          The name of the file that was created.
     */
    public String createFile(final Path filePath) {
        try {
            return Files.createFile(filePath).getFileName().toString();
        } catch (IOException e) {
            throw new DataStreamException("Failed to create new File : " + filePath.getFileName().toString());
        }
    }

    /**
     * Writes the provided content to a file at the specified path using UTF-8
     * encoding.
     *
     * @param  filePath            The path of the file to which the content
     *                             will be written.
     * @param  fileContent         The content to be written to the file.
     * @return                     The path of the file where the content was
     *                             successfully written.
     * @throws DataStreamException If an I/O error occurs while writing the
     *                             content to the file.
     */
    public Path writeToFile(final Path filePath, final @NonNull String fileContent) {
        try {
            return Files.write(filePath, fileContent.getBytes(UTF_8));
        } catch (IOException e) {
            throw new DataStreamException("Failed to write content to file: " + filePath.toAbsolutePath(), e);
        }
    }

    /**
     * Writes the provided content to a file at the specified path using UTF-8
     * encoding.
     *
     * @param  filePath            The path of the file to which the content
     *                             will be written.
     * @param  fileContent         The content to be written to the file.
     * @return                     The path of the file where the content was
     *                             successfully written.
     * @throws DataStreamException If an I/O error occurs while writing the
     *                             content to the file.
     */
    public Path writeToFile(final Path filePath, final @NonNull Iterable<? extends CharSequence> fileContent) {
        try {
            return Files.write(filePath, fileContent, UTF_8);
        } catch (IOException e) {
            throw new DataStreamException("Failed to write content to file: " + filePath.toAbsolutePath(), e);
        }
    }

    /**
     * Copies a file from the source path to the destination path.
     *
     * @param  sourceFile          the path of the file to copy
     * @param  destinationFile     the path to copy the file to
     * @throws DataStreamException if an error occurs while copying the file
     */
    public void copyFile(final Path sourceFile, final Path destinationFile) {
        try {
            Files.copy(sourceFile, destinationFile, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DataStreamException(format("Failed to copy file from [%s] to [%s]: %s",
                sourceFile.toAbsolutePath(), destinationFile.toAbsolutePath(), e.getMessage()));
        }
    }

    /**
     * "Find the first file in the target directory that starts with the given
     * file name."
     * <p>
     * The function takes two parameters:
     * <p>
     * * targetDir - the directory to search * fileName - the name of the file
     * to find
     * <p>
     * The function returns a Path object that represents the file that was
     * found. If no file was found, the function returns null
     *
     * @param  targetDir The directory to search in.
     * @param  fileName  The name of the file you're looking for.
     * @return           A Path object
     */
    public static Path findFile(final Path targetDir, final String fileName) {
        try (var pathStream = Files.list(targetDir)) {
            return pathStream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().startsWith(fileName))
                    .findFirst().orElse(null);
        } catch (IOException exception) {
            return null;
        }
    }

    /**
     * Returns an input stream for reading from the file at the given file path.
     * This method attempts to load the file from several locations in the
     * following order:
     * <ol>
     * <li>The file is loaded from the current thread's context class loader as
     * a resource</li>
     * <li>The file is loaded from the file system using the
     * {@link #newFileStream(String)} method</li>
     * <li>The file is loaded from the class loader of the {@link Resources}
     * class as a resource</li>
     * <li>The file is loaded from the package of the {@link Resources} class as
     * a resource</li>
     * </ol>
     * The method returns the first non-null input stream that is successfully
     * loaded from one of these locations.
     *
     * @param  filePath            the path to the file to read from
     * @return                     an input stream for the file
     * @throws DataStreamException if the file cannot be loaded from any of the
     *                             locations or an error occurs while opening
     *                             the stream
     */
    public InputStream fileStream(final String filePath) {
        Stream<Supplier<InputStream>> streamSupplier = Stream.of(
            () -> Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath),
            () -> newFileStream(filePath),
            () -> Resources.class.getClassLoader().getResourceAsStream(filePath),
            () -> Resources.class.getResourceAsStream(filePath));
        return streamSupplier.parallel()
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(
                    () -> new DataStreamException(format("Failed to load file [%s] as a stream. " +
                            "Make sure the file exists and is accessible.",
                        filePath)));
    }

    /**
     * Given a file name, return a Path object that points to the file in the
     * test resources' directory.
     *
     * @param  fileName The name of the file to be read.
     * @return          A Path object
     */
    public Path ofTest(final String fileName) {
        return of(TEST_RESOURCES + File.separator + fileName);
    }

    /**
     * Returns an input stream for reading from the file at the given file path.
     *
     * @param  filePath the path to the file to read from
     * @return          an input stream for the file, or {@code null} if the
     *                  file does not exist or an error occurs while opening the
     *                  stream
     */
    public static InputStream newFileStream(String filePath) {
        Path path = Path.of(filePath);
        if (Files.exists(path)) {
            try {
                return Files.newInputStream(path);
            } catch (IOException ignored) {
                // Gobble exception
            }
        }
        return null;
    }

    /**
     * Returns a new output stream that writes to the file with the specified
     * file path.
     *
     * @param  filePath the path of the file to write to
     * @return          a new output stream that writes to the specified file
     */
    @SneakyThrows
    public static OutputStream newOutputStream(Path filePath) {
        return Files.newOutputStream(filePath);
    }

    /**
     * Returns a new URL object by parsing the given URL string.
     *
     * @param  urlStr                   the URL string to be parsed into a URL
     *                                  object
     * @return                          the URL object representing the parsed
     *                                  URL string
     * @throws IllegalArgumentException if the URL string is invalid and cannot
     *                                  be parsed
     */
    public URL toURL(String urlStr) {
        return tryURL(urlStr)
                .orElseThrow(() -> new IllegalArgumentException("Invalid URL string: " + urlStr));
    }

    /**
     * Returns an Optional containing a new URL object by parsing the given URL
     * string, or an empty Optional if the URL string is invalid.
     *
     * @param  urlStr the URL string to be parsed into a URL object
     * @return        an Optional containing the URL object representing the
     *                parsed URL string, or an empty Optional if the URL string
     *                is invalid
     */
    public Optional<URL> tryURL(String urlStr) {
        try {
            return Optional.of(new URI(urlStr).toURL());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
