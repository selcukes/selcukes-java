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

import io.github.selcukes.collections.Resources;
import io.github.selcukes.collections.StringHelper;
import io.github.selcukes.databind.annotation.DataFile;
import io.github.selcukes.databind.exception.DataMapperException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static io.github.selcukes.collections.Resources.TEST_RESOURCES;
import static io.github.selcukes.collections.Resources.findFile;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class DataFileHelper<T> {
    private final Class<T> dataClass;
    private final DataFile dataFile;
    private boolean isNewFile;

    private DataFileHelper(final Class<T> dataClass) {
        this.dataClass = dataClass;
        this.dataFile = ofNullable(dataClass.getDeclaredAnnotation(DataFile.class))
                .orElseThrow(() -> new DataMapperException(format("Data Class[%s] must have @DataFile annotation.",
                    dataClass.getSimpleName())));
    }

    public static <T> DataFileHelper<T> getInstance(final Class<T> dataClass) {
        return new DataFileHelper<>(dataClass);
    }

    public void setNewFile(boolean newFile) {
        isNewFile = newFile;
    }

    public String getFileName() {
        if (!this.dataFile.fileName().isEmpty()) {
            return DataSubstitutor.substituteSystemProperty(this.dataFile.fileName());
        }
        if (isStream()) {
            throw new DataMapperException("Please provide fileName to perform stream loader");
        }
        final var fileName = StringHelper.toSnakeCase(this.dataClass.getSimpleName());
        final var folder = getFolder();

        return ofNullable(findFile(folder, fileName))
                .map(path -> path.getFileName().toString())
                .orElseGet(isNewFile ? () -> newFile(folder, fileName) : () -> {
                    throw new DataMapperException(format("File [%s] not found.", fileName));
                });
    }

    public boolean isStream() {
        return this.dataFile.streamLoader();
    }

    private Path getFolder() {
        var folder = Optional.of(this.dataFile.folderPath())
                .filter(StringHelper::isNonEmpty)
                .map(DataSubstitutor::substituteSystemProperty)
                .filter(StringHelper::isNonEmpty)
                .orElse(TEST_RESOURCES);
        return Resources.isDirectory(folder);
    }

    public Path newFilePath(String fileName) {
        return getFolder().resolve(fileName);
    }

    public Path getPath(String fileName) {
        var filePath = newFilePath(fileName);
        if (Files.exists(filePath)) {
            return filePath;
        } else {
            throw new DataMapperException(format("File [%s] not found at path [%s].", fileName, filePath));
        }
    }

    private String newFile(final Path folder, final String fileName) {
        var newFileName = fileName.contains(".") ? fileName : fileName + ".json";
        var newFilePath = folder.resolve(newFileName);
        return Resources.createFile(newFilePath);
    }
}
