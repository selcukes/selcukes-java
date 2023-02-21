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

package io.github.selcukes.databind.tests;

import io.github.selcukes.databind.csv.CsvMapper;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.Objects;

public class CsvTest {
    @SneakyThrows
    @Test
    public void csvDataReaderTest() {
        Path filePath = Path.of(
            Objects.requireNonNull(CsvTest.class.getClassLoader().getResource("employee.csv")).toURI());
        var csvData = CsvMapper.parse(filePath);

        csvData.forEach(map -> map.computeIfPresent("ID", (k, v) -> {
            String phone = map.get("Phone");
            return map.get("Country").substring(0, 3).toUpperCase() + "_DDA_" + phone.substring(phone.length() - 4);
        }));
        csvData.forEach(System.out::println);
    }

}
