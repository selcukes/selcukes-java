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
import io.github.selcukes.databind.utils.Resources;
import org.testng.annotations.Test;

import java.util.Map;

import static io.github.selcukes.databind.csv.CsvMapper.CSV_STRIP_REGEX;

public class CsvTest {

    @Test
    public void csvDataReaderTest() {
        var filePath = Resources.ofTest("employee.csv");
        var table = CsvMapper.parse(filePath, CSV_STRIP_REGEX);

        Map<String, String> keyMapping = Map.of("Name", "FullName");
        table.updateColumnName(keyMapping);

        table.updateRows(row -> {
            // Update ID Column values
            if (row.get("ID").isEmpty()) {
                row.put("ID", updateID(row.get("Phone"), row.get("Country")));
            }
            return row;
        });
        table.getRows().forEach(System.out::println);
    }

    private String updateID(String phone, String country) {
        var countryCode = country.substring(0, 3).toUpperCase();
        var lastFourDigits = phone.substring(phone.length() - 4);
        return countryCode + "_DDA_" + lastFourDigits;
    }

}
