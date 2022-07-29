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

package io.github.selcukes.databind.excel;

import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.DataFileHelper;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

/**
 * This class is an Excel mapper to parse Excel Sheet to stream of entityClass objects
 */
@UtilityClass
public class ExcelMapper {
    /**
     * Parses the Excel file to an Entity Class.
     * It takes a class as input and returns a stream of objects of that class
     *
     * @param <T>         the Class type.
     * @param entityClass The class of the entity to be parsed
     * @return the Stream of Entity class objects
     */
    public <T> Stream<T> parse(final Class<T> entityClass) {
        final DataFileHelper<T> dataFile = DataFileHelper.getInstance(entityClass);
        final String fileName = dataFile.getFileName();
        int extensionIndex = fileName.lastIndexOf('.');
        final String extension = fileName.substring(extensionIndex + 1);
        if (!extension.equalsIgnoreCase("xlsx")) {
            throw new DataMapperException(String.format("File [%s] not found.",
                    fileName.substring(0, extensionIndex) + ".xlsx"));
        }
        ExcelParser<T> excelMapper = new ExcelParser<>(entityClass);
        return excelMapper.parse(dataFile.getPath());
    }
}
