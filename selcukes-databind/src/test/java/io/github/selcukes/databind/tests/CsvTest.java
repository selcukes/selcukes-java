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

import io.github.selcukes.collections.Resources;
import io.github.selcukes.databind.csv.CsvMapper;
import org.testng.annotations.Test;

import java.util.Map;

import static io.github.selcukes.databind.csv.CsvMapper.CSV_STRIP_REGEX;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CsvTest {

    @Test
    public void csvDataReaderTest() {
        var filePath = Resources.ofTest("employee.csv");
        var table = CsvMapper.parse(filePath, CSV_STRIP_REGEX);
        assertFalse(table.isEmpty(), "Table should not be empty");

        Map<String, String> keyMapping = Map.of("Name", "FullName");
        table.renameColumn(keyMapping);
        assertTrue(table.getColumns().contains("FullName"), "Column 'FullName' should be present");

        table.updateRows(row -> {
            // Update ID Column values
            if (row.get("ID").isEmpty()) {
                row.put("ID", updateID(row.get("Phone"), row.get("Country")));
            }
            return row;
        });
        assertFalse(table.getColumnEntries("ID").stream().anyMatch(String::isEmpty), "ID column should not be empty");

        var filePath1 = Resources.ofTest("employee1.csv");
        CsvMapper.write(filePath1, table);
        var table1 = CsvMapper.parse(filePath1, CSV_STRIP_REGEX);

        assertFalse(table1.isEmpty(), "Written table should not be empty");
        assertEquals(table1, table, "Written table should be the same as the original table");
    }

    private String updateID(String phone, String country) {
        var countryCode = country.substring(0, 3).toUpperCase();
        var lastFourDigits = phone.substring(phone.length() - 4);
        return countryCode + "_DDA_" + lastFourDigits;
    }
}
