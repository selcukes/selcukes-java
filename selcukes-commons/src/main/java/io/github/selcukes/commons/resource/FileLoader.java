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

package io.github.selcukes.commons.resource;

import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.commons.helper.DataFileHelper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileLoader {
    public static <T> T parse(final Class<T> resourceClass) {
        final DataFileHelper<T> dataFile = DataFileHelper.getInstance(resourceClass);
        final String fileName = dataFile.getFileName();
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        final ResourceReader resourceReader = lookup(extension);
        return resourceReader.parse(dataFile.getPath(), resourceClass);
    }

    private static ResourceReader lookup(final String extension) {
        switch (extension.toLowerCase()) {
            case "yaml":
                return new YamlResource();
            case "json":
                return new JsonResource();
            default:
                throw new SelcukesException("Resource File not supported...");
        }

    }
}
