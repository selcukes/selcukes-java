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

import io.github.selcukes.collections.DataTable;
import io.github.selcukes.collections.Resources;
import io.github.selcukes.databind.excel.ExcelMapper;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ExcelWriterTest {
    @Test
    public void excelWrite() {
        DataTable<String, Object> input = DataTable.of(
            new LinkedHashMap<>(Map.of("ID", 1, "Name", "John Doe", "Age", 30, "IsEmployed", false)),
            new LinkedHashMap<>(Map.of("ID", 2, "Name", "Jane Smith", "Age", 40, "IsEmployed", false)),
            new LinkedHashMap<>(Map.of("ID", 3, "Name", "Tom", "Age", 35, "IsEmployed", false)));

        String fileName = Resources.ofTest("output.xlsx").toString();
        String sheetName = "Sheet1";
        ExcelMapper.write(input, fileName, sheetName);
        var output = ExcelMapper.parse(fileName, sheetName);
        assertEquals(output.toString(), input.toString());
    }
}
