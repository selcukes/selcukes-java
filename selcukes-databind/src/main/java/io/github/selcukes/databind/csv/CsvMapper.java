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

import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.collections.Lists;
import io.github.selcukes.databind.collections.Streams;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CsvMapper {
    // A regex that splits a line by commas, but ignores commas that are inside
    // double quotes.
    private static final String CSV_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    // A regex that removes double quotes from the beginning and end of a field.
    private static final String DOUBLE_QUOTES_REGEX = "^\"|\"$";
    // A regex that splits a line by commas, but ignores commas that are inside
    // double quotes.
    public static final String CSV_STRIP_REGEX = "\\s*,(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)\\s*";

    /**
     * It takes a file path and a regex, reads the file line by line, splits
     * each line by the regex, removes the double quotes, and returns a list of
     * maps
     *
     * @param  filePath The path to the CSV file.
     * @param  regex    The regex to split the line by.
     * @return          A list of maps.
     */
    public List<Map<String, String>> parse(Path filePath, String regex) {
        try (var lines = Files.lines(filePath)) {
            return Streams.toListOfMap(
                Lists.of(lines, line -> Arrays.stream(line.split(regex))
                        .map(field -> field.replaceAll(DOUBLE_QUOTES_REGEX, ""))
                        .collect(Collectors.toCollection(LinkedList::new))));
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing CSV File: ", e);
        }
    }

    /**
     * It takes a CSV file, reads it line by line, splits each line into a
     * stream of fields, removes double quotes, trims each field, collects the
     * fields into a list, collects the list of fields into a list of lists, and
     * finally converts the list of lists into a list of maps
     *
     * @param  filePath The path to the CSV file.
     * @return          A list of maps.
     */
    public List<Map<String, String>> parse(Path filePath) {
        return parse(filePath, CSV_REGEX);
    }
}
