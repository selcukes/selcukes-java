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

package io.github.selcukes.databind.utils;

import io.github.selcukes.databind.exception.DataMapperException;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@UtilityClass
public class Resources {
    public static final String USER_DIR = System.getProperty("user.dir");
    public static final String TEST_RESOURCES = "src/test/resources";

    /**
     * If the file path is absolute, return it, otherwise return the absolute
     * path of the file path
     *
     * @param  filePath The path to the file.
     * @return          A Path object
     */
    public Path of(final String filePath) {
        return Path.of(filePath).isAbsolute() ? Path.of(filePath) : Path.of(USER_DIR).resolve(filePath);
    }

    /**
     * "If the folder is not a directory, throw an exception."
     * <p>
     * The function isDirectory() is a good example of a function that is easy
     * to understand. It's easy to understand because it's short, and it's easy
     * to understand because it's written in a way that makes it easy to
     * understand
     *
     * @param  folder The folder to check.
     * @return        A Path object.
     */
    public Path isDirectory(final String folder) {
        var folderPath = of(folder);
        if (!Files.isDirectory(folderPath)) {
            throw new DataMapperException(format("%s is not a directory.", folder));
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
            throw new DataMapperException("Failed to create new File : " + filePath.getFileName().toString());
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
     * "If the file exists, return it as a stream, otherwise throw an
     * exception."
     *
     * @param  fileName The name of the file to be loaded.
     * @return          InputStream
     */
    public InputStream fileStream(final String fileName) {
        return ofNullable(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName))
                .orElseThrow(() -> new DataMapperException(
                    format("Failed to load file [%s] as a stream from classpath. " +
                            "Make sure the file exists and is included in the classpath.",
                        fileName)));
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
}
