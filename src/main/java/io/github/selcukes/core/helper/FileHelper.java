/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.helper;


import io.github.selcukes.core.exception.WebDriverBinaryException;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class FileHelper {
    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);
    protected static final String SUPPORT_FOLDER = "support";
    public static final String RESOURCE_SEPARATOR = "/";

    public static String driversFolder(String path) {
        File file = new File(path);
        for (String item : Objects.requireNonNull(file.list())) {

            if (SUPPORT_FOLDER.equals(item)) {
                return file.getAbsolutePath() + RESOURCE_SEPARATOR + SUPPORT_FOLDER + RESOURCE_SEPARATOR;
            }
        }
        return driversFolder(file.getParent());
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

    public static String systemFilePath(String filePath) {
        String fileSeparator = System.getProperty("file.separator");

        if (StringUtils.isNotBlank(fileSeparator) && "\\".equals(fileSeparator)) {
            int beginIndex = 0;

            if (filePath.startsWith(RESOURCE_SEPARATOR)) {
                beginIndex = 1;
            }

            return filePath.substring(beginIndex).replace(RESOURCE_SEPARATOR, "\\");
        }

        return filePath;
    }

    public static void createDirectory(File dirName) {
        boolean dirExists = dirName.exists();
        if (!dirExists) {
            logger.debug(() -> "Creating directory: " + dirName.getName());
            dirExists = dirName.mkdirs();
            if (dirExists) {
                logger.debug(() -> dirName.getName() + " directory created...");
            }
        }
    }

    public static void deleteFilesInDirectory(File dirName) throws IOException {
        try {
            FileUtils.cleanDirectory(dirName);
        } catch (IOException e) {
            throw new IOException("\"" + dirName + "\" is not a directory or a file to delete.", e);
        }
    }

    public static void deleteFile(File fileName) {
        if (fileName.exists())
            FileUtils.deleteQuietly(fileName);
    }
    public static void renameFile(File from, File to){
        if (!from.renameTo(to)) {
            logger.error(() ->"Failed to rename " + from + " to " + to);
        }
    }

}

