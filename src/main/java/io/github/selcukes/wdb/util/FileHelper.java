/*
 *
 *  * Copyright (c) Ramesh Babu Prudhvi.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.selcukes.wdb.util;

import io.github.selcukes.core.exception.WebDriverBinaryException;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;


public class FileHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

    private FileHelper() {

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

    public static void createDirectory(File dirName) {
        boolean dirExists = dirName.exists();
        if (!dirExists) {
            logger.debug(()->"Creating directory: " + dirName.getName());
            dirExists = dirName.mkdirs();
            if (dirExists) {
                logger.debug(()->dirName.getName() + " directory created...");
            }
        }
    }
}
