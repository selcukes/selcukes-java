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
import io.github.selcukes.databind.utils.Streams;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class CsvMapper {

    /**
     * It takes a CSV file, reads it line by line, splits each line by comma,
     * removes the quotes, and returns a list of maps
     *
     * @param  filePath The path to the file to be parsed.
     * @return          A list of maps.
     */
    public List<Map<String, String>> parse(Path filePath) {
        try (var lines = Files.lines(filePath)) {
            return Streams.toListOfMap(lines.parallel()
                    .map(line -> Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)").splitAsStream(line)
                            .map(field -> field.trim().replace("\"", ""))
                            .collect(Collectors.toCollection(LinkedList::new)))
                    .collect(Collectors.toCollection(LinkedList::new)));
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing CSV File: ", e);
        }
    }
}
