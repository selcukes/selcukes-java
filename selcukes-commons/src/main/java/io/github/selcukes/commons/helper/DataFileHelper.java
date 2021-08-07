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

package io.github.selcukes.commons.helper;

import io.github.selcukes.commons.annotation.DataFile;
import io.github.selcukes.commons.exception.SelcukesException;

import java.io.File;

import static java.lang.System.getProperty;
import static java.util.Objects.requireNonNull;

public class DataFileHelper<T> {
    public static <T> DataFileHelper<T> getInstance(final Class<T> dataClass) {
        return new DataFileHelper<>(dataClass);
    }

    private final Class<T> dataClass;
    private final DataFile dataFile;

    private DataFileHelper(final Class<T> dataClass) {
        this.dataClass = dataClass;
        if (!this.dataClass.isAnnotationPresent(DataFile.class)) {
            throw new SelcukesException("Data Class must have @DataFile annotation.");
        }
        this.dataFile = dataClass.getAnnotation(DataFile.class);
    }

    public String getFileName() {
        if (!this.dataFile.fileName().isEmpty()) {
            return this.dataFile.fileName();
        }
        final String fileName = StringUtils.toSnakeCase(this.dataClass.getSimpleName());
        final File folder = new File(getFolder());
        final File[] files = folder.listFiles((d, f) -> f.startsWith(fileName));
        if (requireNonNull(files).length == 0) {
            throw new SelcukesException(String.format("File [%s] not found.", fileName));
        }
        return files[0].getName();
    }

    public String getFolder() {
        String folder = "src/test/resources";
        if (!this.dataFile.folderPath().isEmpty()) {
            folder = this.dataFile.folderPath();
        }
        return isDirectory(folder);
    }

    public String getPath() {
        return String.format("%s/%s/%s", getRootFolder(), getFolder(), getFileName());
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
            throw new SelcukesException(String.format("%s is not a directory.", folder));
        }
        return folder;
    }
}
