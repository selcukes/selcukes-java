/*
 *
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
 *
 */

package io.github.selcukes.databind.utils;


import io.github.selcukes.databind.annotation.DataFile;
import io.github.selcukes.databind.exception.DataMapperException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

public class DataFileHelper<T> {
    private final Class<T> dataClass;
    private final DataFile dataFile;
    private boolean isNewFile;

    public void setNewFile(boolean newFile) {
        isNewFile = newFile;
    }

    private DataFileHelper(final Class<T> dataClass) {
        this.dataClass = dataClass;
        this.dataFile = ofNullable(dataClass.getDeclaredAnnotation(DataFile.class))
            .orElseThrow(() -> new DataMapperException(format("Data Class[%s] must have @DataFile annotation.",
                dataClass.getSimpleName())
            ));
    }

    public static <T> DataFileHelper<T> getInstance(final Class<T> dataClass) {
        return new DataFileHelper<>(dataClass);
    }

    public String getFileName() {
        if (!this.dataFile.fileName().isEmpty()) {
            return this.dataFile.fileName();
        }
        if (isStream()) {
            throw new DataMapperException("Please provide fileName to perform stream loader");
        }
        final var fileName = StringHelper.toSnakeCase(this.dataClass.getSimpleName());
        final var folder = getFolder();

        return ofNullable(findFile(folder, fileName))
            .map(path -> path.getFileName().toString())
            .orElseGet(() -> {
                if (isNewFile) {
                    return newFile(folder, fileName);
                } else
                    throw new DataMapperException(String.format("File [%s] not found.", fileName));
            });
    }

    public InputStream fileStream() {
        try {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(getFileName());
        } catch (Exception e) {
            throw new DataMapperException(String.format("Failed to perform stream loader for a File [%s].", getFileName()));
        }
    }

    public boolean isStream() {
        return this.dataFile.streamLoader();
    }

    public Path getFolder() {
        var folder = this.dataFile.folderPath();
        if (folder.isEmpty()) {
            folder = "src/test/resources";
        }
        return this.isDirectory(this.getRootFolder().resolve(folder).toString());
    }

    public Path getPath() {
        return getFolder().resolve(Path.of(getFileName()));
    }

    private Path getRootFolder() {
        var root = this.dataFile.rootFolder();
        if (root.isEmpty()) {
            root = getProperty("user.dir");
        }
        return isDirectory(root);
    }

    private Path isDirectory(final String folder) {
        final var dir = new File(folder);
        if (!dir.isDirectory()) {
            throw new DataMapperException(String.format("%s is not a directory.", folder));
        }
        return Path.of(folder);
    }

    private String newFile(final Path folder, final String fileName) {
        var newFileName = fileName.contains(".") ? fileName : fileName + ".json";
        var newFilePath = folder.resolve(newFileName);
        try {
            return Files.createFile(newFilePath).getFileName().toString();
        } catch (IOException e) {
            throw new DataMapperException("Filed to creating new File : " + newFileName);
        }
    }

    private Path findFile(final Path targetDir, final String fileName) {
        try (var stream = Files.list(targetDir)) {
            return stream.filter(p -> {
                if (Files.isRegularFile(p)) {
                    return p.getFileName().toString().startsWith(fileName);
                } else {
                    return false;
                }
            }).findFirst().orElse(null);
        } catch (IOException exception) {
            return null;
        }
    }
}
