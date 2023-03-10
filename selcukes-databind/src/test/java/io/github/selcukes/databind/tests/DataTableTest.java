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

import io.github.selcukes.databind.utils.DataTable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DataTableTest {
    private DataTable<String> dataTable;
    private Map<String, String> row2;
    private List<String> columns;

    @BeforeMethod
    public void beforeMethod() {
        columns = List.of("Name", "Age", "Country");
        dataTable = new DataTable<>(columns);
        var row1 = Map.of("Name", "Alice", "Age", "25", "Country", "USA");
        row2 = Map.of("Name", "Bob", "Age", "35", "Country", "Canada");
        dataTable.addRow(row1);
        dataTable.addRow(row2);
    }

    @Test(testName = "Test addRow")
    void testAddRow() {
        var newRow = Map.of("Name", "Charlie", "Age", "30", "Country", "UK");
        dataTable.addRow(newRow);
        assertEquals(3, dataTable.getRows().size());
        assertTrue(dataTable.contains(newRow));
    }

    @Test(testName = "Test addRows")
    void testAddRows() {
        var row3 = Map.of("Name", "Dave", "Age", "40", "Country", "Australia");
        var row4 = Map.of("Name", "Eve", "Age", "20", "Country", "France");
        var newRows = List.of(row3, row4);
        dataTable.addRows(newRows);
        assertEquals(4, dataTable.getRows().size());
        assertTrue(dataTable.contains(row3));
        assertTrue(dataTable.contains(row4));
    }

    @Test(testName = "Test getRows")
    void testGetRows() {
        List<Map<String, String>> rows = dataTable.getRows();

        assertEquals(2, rows.size());

        Map<String, String> row1 = rows.get(0);
        assertEquals("Alice", row1.get("Name"));
        assertEquals("25", row1.get("Age"));
        assertEquals("USA", row1.get("Country"));

        Map<String, String> row2 = rows.get(1);
        assertEquals("Bob", row2.get("Name"));
        assertEquals("35", row2.get("Age"));
        assertEquals("Canada", row2.get("Country"));
    }

    @Test(testName = "Test getRow")
    void testGetRow() {
        Map<String, String> actualRow = dataTable.getRow(row -> row.get("Name").equals("Bob"));
        assertEquals(row2, actualRow);
    }

    @Test(testName = "Test getRowsGroupedByColumn")
    void testGetRowsGroupedByColumn() {
        var rowA = Map.of("id", "1", "name", "John", "age", "20");
        var rowB = Map.of("id", "2", "name", "Jane", "age", "25");
        var rowC = Map.of("id", "3", "name", "Bob", "age", "30");
        var rowD = Map.of("id", "4", "name", "Alice", "age", "20");
        var rowE = Map.of("id", "5", "name", "Tom", "age", "25");
        var rows = List.of(rowA, rowB, rowC, rowD, rowE);
        var table = DataTable.of(rows);
        var rowsByAge = table.getRowsGroupedByColumn("age");
        assertEquals(3, rowsByAge.size());
        var age20Rows = rowsByAge.get("20");
        assertEquals(2, age20Rows.size());
        assertTrue(age20Rows.contains(rowA));
        assertTrue(age20Rows.contains(rowD));

        var age25Rows = rowsByAge.get("25");
        assertEquals(2, age25Rows.size());
        assertTrue(age25Rows.contains(rowB));
        assertTrue(age25Rows.contains(rowE));
    }

    @Test(testName = "Test getColumns")
    public void testGetColumns() {
        assertEquals(dataTable.getColumns(), columns);
    }

    @Test(testName = "Test GetColumnValues")
    public void testGetColumnValues() {
        assertEquals(dataTable.getColumnValues("Name"), List.of("Alice", "Bob"));
    }
}
