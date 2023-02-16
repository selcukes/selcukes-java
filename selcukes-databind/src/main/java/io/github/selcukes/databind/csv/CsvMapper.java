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
import io.github.selcukes.databind.utils.Maps;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CsvMapper {

    public List<Map<String, String>> parse(Path filePath) {
        try (var lines = Files.lines(filePath)) {
            return Maps.asListOfMap(lines.map(line -> line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"))
                    .filter(line -> line.length != 0)
                    .map(Arrays::asList).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing CSV File: ", e);
        }
    }
}
