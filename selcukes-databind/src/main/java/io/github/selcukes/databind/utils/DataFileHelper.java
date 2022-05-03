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
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.System.getProperty;

public class DataFileHelper<T> {
    private final Class<T> dataClass;
    private final DataFile dataFile;
    private boolean isNewFile;

    public void setNewFile(boolean newFile) {
        isNewFile = newFile;
    }

    private DataFileHelper(final Class<T> dataClass) {
        this.dataClass = dataClass;
        if (!this.dataClass.isAnnotationPresent(DataFile.class)) {
            throw new DataMapperException(String.format("Data Class[%s] must have @DataFile annotation.", dataClass.getSimpleName()));
        }
        this.dataFile = dataClass.getAnnotation(DataFile.class);
    }

    public static <T> DataFileHelper<T> getInstance(final Class<T> dataClass) {
        return new DataFileHelper<>(dataClass);
    }

    public String getFileName() {
        if (!this.dataFile.fileName().isEmpty()) {
            return this.dataFile.fileName();
        }
        final String fileName = StringHelper.toSnakeCase(this.dataClass.getSimpleName());
        final Path folder = Paths.get(getFolder());

        Optional<Path> path = Optional.ofNullable(findFile(folder, fileName));
        if (path.isEmpty()) {
            if (isNewFile) {
                return newFile(fileName);
            } else
                throw new DataMapperException(String.format("File [%s] not found.", fileName));
        }
        return path.get().getFileName().toString();
    }

    public String getFolder() {
        String folder = "src/test/resources";
        if (!this.dataFile.folderPath().isEmpty()) {
            folder = this.dataFile.folderPath();
        }
        return isDirectory(Paths.get(getRootFolder(), folder).toString());
    }

    public String getPath() {
        return String.format("%s/%s", getFolder(), getFileName());
    }

    public String getRootFolder() {
        String root = getProperty("user.dir");
        if (!this.dataFile.rootFolder().isEmpty()) {
            root = this.dataFile.rootFolder();
        }

        return isDirectory(root);
    }

    private String isDirectory(final String folder) {
        final File dir = new File(folder);
        if (!dir.isDirectory()) {
            throw new DataMapperException(String.format("%s is not a directory.", folder));
        }
        return folder;
    }

    private String newFile(String fileName) {
        String newFileName = fileName.contains(".") ? fileName : fileName + ".json";
        Path newFilePath = Path.of(String.format("%s/%s", getFolder(), newFileName));
        try {
            return Files.createFile(newFilePath).getFileName().toString();
        } catch (IOException e) {
            throw new DataMapperException("Filed to creating new File : " + newFileName);
        }
    }

    @SneakyThrows
    private Path findFile(Path targetDir, String fileName) {
        return Files.list(targetDir).filter((p) -> {
            if (Files.isRegularFile(p)) {
                return p.getFileName().toString().startsWith(fileName);
            } else {
                return false;
            }
        }).findFirst().orElse(null);
    }
}
