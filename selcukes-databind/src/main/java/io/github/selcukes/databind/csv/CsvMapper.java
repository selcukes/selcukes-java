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

package io.github.selcukes.databind.csv;

import io.github.selcukes.databind.collections.DataTable;
import io.github.selcukes.databind.collections.Lists;
import io.github.selcukes.databind.collections.Streams;
import io.github.selcukes.databind.exception.DataMapperException;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class CsvMapper {
    private static final String CSV_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final String DOUBLE_QUOTES_REGEX = "^\"|\"$";
    public static final String CSV_STRIP_REGEX = "\\s*,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\\s*";

    /**
     * Parses a CSV file at the given file path using the specified regex.
     *
     * @param  filePath            the path to the CSV file
     * @param  regex               the regex to split the line by
     * @return                     a {@code DataTable<String, String>}
     *                             representing the parsed CSV file
     * @throws DataMapperException if an error occurs while parsing the file
     */
    public DataTable<String, String> parse(Path filePath, String regex) {
        try (var lines = Files.lines(filePath)) {
            var linesOnWords = Lists.of(lines, line -> Arrays.stream(line.split(regex))
                    .map(field -> field.replaceAll(DOUBLE_QUOTES_REGEX, ""))
                    .collect(Collectors.toList()));
            return Streams.toTable(linesOnWords);
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing CSV File: ", e);
        }
    }

    /**
     * Parses a CSV file at the given file path using the specified regex.
     *
     * @param  filePath            the path to the CSV file
     * @return                     a {@code DataTable<String, String>}
     *                             representing the parsed CSV file
     * @throws DataMapperException if an error occurs while parsing the file
     */
    public DataTable<String, String> parse(Path filePath) {
        return parse(filePath, CSV_REGEX);
    }
}
