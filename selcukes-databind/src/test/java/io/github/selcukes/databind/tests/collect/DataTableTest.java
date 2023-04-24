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

package io.github.selcukes.databind.tests.collect;

import io.github.selcukes.databind.collections.DataTable;
import io.github.selcukes.databind.collections.Maths;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DataTableTest {
    private DataTable<String, String> dataTable;
    private Set<String> columns;

    @BeforeMethod
    public void beforeMethod() {
        columns = Set.of("Name", "Age", "Country");
        dataTable = new DataTable<>();
        var row1 = Map.of("Name", "Alice", "Age", "25", "Country", "USA");
        Map<String, String> row2 = Map.of("Name", "Bob", "Age", "35", "Country", "Canada");
        dataTable.addRow(row1);
        dataTable.addRow(row2);
    }

    @Test(testName = "Test addRow")
    void testAddRow() {
        var newRow = Map.of("Name", "Charlie", "Age", "30", "Country", "UK");
        dataTable.addRow(newRow);
        assertEquals(dataTable.size(), 3);
        assertTrue(dataTable.contains(newRow));
    }

    @Test(testName = "Test addRows")
    void testAddRows() {
        var row3 = Map.of("Name", "Dave", "Age", "40", "Country", "Australia");
        var row4 = Map.of("Name", "Eve", "Age", "20", "Country", "France");
        var newRows = List.of(row3, row4);
        dataTable.addRows(newRows);
        assertEquals(dataTable.size(), 4);
        assertTrue(dataTable.contains(row3));
        assertTrue(dataTable.contains(row4));
    }

    @Test(testName = "Test getRows")
    void testGetRows() {
        var rows = dataTable.rows().collect(Collectors.toList());

        assertEquals(rows.size(), 2);

        Map<String, String> row1 = rows.get(0);
        assertEquals(row1.get("Name"), "Alice");
        assertEquals(row1.get("Age"), "25");
        assertEquals(row1.get("Country"), "USA");

        Map<String, String> row2 = rows.get(1);
        assertEquals(row2.get("Name"), "Bob");
        assertEquals(row2.get("Age"), "35");
        assertEquals(row2.get("Country"), "Canada");
    }

    @Test(testName = "Test groupByColumn")
    void testGroupByColumn() {
        var rowA = Map.of("id", "1", "name", "John", "age", "20");
        var rowB = Map.of("id", "2", "name", "Jane", "age", "25");
        var rowC = Map.of("id", "3", "name", "Bob", "age", "30");
        var rowD = Map.of("id", "4", "name", "Alice", "age", "20");
        var rowE = Map.of("id", "5", "name", "Tom", "age", "25");
        var table = DataTable.of(rowA, rowB, rowC, rowD, rowE);
        var rowsByAge = table.groupByColumn("age");
        assertEquals(rowsByAge.size(), 3);
        var age20Rows = rowsByAge.get("20");
        assertEquals(age20Rows.size(), 2);
        assertTrue(age20Rows.contains(rowA));
        assertTrue(age20Rows.contains(rowD));

        var age25Rows = rowsByAge.get("25");
        assertEquals(age25Rows.size(), 2);
        assertTrue(age25Rows.contains(rowB));
        assertTrue(age25Rows.contains(rowE));
    }

    @Test(testName = "Test getColumns")
    public void testGetColumns() {
        assertEquals(Set.copyOf(dataTable.getColumns()), columns);
    }

    @Test(testName = "Test GetColumnValues")
    public void testGetColumnValues() {
        assertEquals(dataTable.getColumnEntries("Name"), List.of("Alice", "Bob"));
    }

    @Test(testName = "Test updateRows")
    void testUpdateRows() {
        Map<String, Object> rowA = Map.of("ID", 1, "Name", "John Doe", "Age", 25, "IsEmployed", false);
        Map<String, Object> rowB = Map.of("ID", 2, "Name", "Jane Smith", "Age", 30, "IsEmployed", false);
        Map<String, Object> rowC = Map.of("ID", 3, "Name", "Tom", "Age", 25, "IsEmployed", false);
        var dataTable = DataTable.of(rowA, rowB, rowC);
        assertEquals(dataTable.size(), 3);
        assertTrue(dataTable.contains(rowA));
        assertTrue(dataTable.contains(rowB));
        assertTrue(dataTable.contains(rowC));
        dataTable.updateRows(row -> {
            row.put("Age", (int) row.get("Age") + 5);
            row.put("IsEmployed", !(boolean) row.get("IsEmployed"));
            return row;
        });
        dataTable.getColumnEntries("IsEmployed")
                .stream()
                .map(value -> (boolean) value)
                .forEach(Assert::assertTrue);
        assertEquals(dataTable.get(0).get("Age"), 30);
    }

    @Test
    public void testAddColumn() {
        dataTable.addColumn("IsEmployed", "true");
        var countries = dataTable.getColumnEntries("IsEmployed");
        var expected = List.of("true", "true");
        assertEquals(countries, expected);
    }

    @Test
    public void testUpdateCell() {
        dataTable.updateCell(1, "Age", "45");
        var ages = dataTable.getColumnEntries("Age");
        var expected = List.of("25", "45");
        assertEquals(ages, expected);
    }

    @Test
    public void testSortByColumn() {
        Map<String, Object> rowA = Map.of("ID", 1, "Name", "John Doe", "Age", 30, "IsEmployed", false);
        Map<String, Object> rowB = Map.of("ID", 2, "Name", "Jane Smith", "Age", 40, "IsEmployed", false);
        Map<String, Object> rowC = Map.of("ID", 3, "Name", "Tom", "Age", 35, "IsEmployed", false);
        var table = DataTable.of(rowA, rowB, rowC);
        Comparator<Object> ageComparator = Comparator.comparing(Object::toString);
        table.sortByColumn("Age", ageComparator);
        var actualAges = table.getColumnEntries("Age");
        var expectedAges = List.of(30, 35, 40);
        assertEquals(expectedAges, actualAges);

        Comparator<Object> nameComparator = Comparator.comparing(Object::toString);
        table.sortByColumn("Name", nameComparator.reversed());
        var actualNames = table.getColumnEntries("Name");
        var expectedNames = List.of("Tom", "John Doe", "Jane Smith");
        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void testRemoveRow() {
        dataTable.removeRow(1);
        var names = dataTable.getColumnEntries("Name");
        var expected = List.of("Alice");
        assertEquals(names, expected);
        dataTable.removeRows(row -> row.get("Name").equals("Alice"));
        assertTrue(dataTable.isEmpty());
    }

    @Test(testName = "Test GetCellValues")
    public void testGetCellValues() {
        assertEquals(dataTable.getCellValue(1, "Name"), "Bob");
    }

    @Test
    public void testJoin() {
        var table1Rows = List.of(
            Map.of("id", 1, "name", "Alice"),
            Map.of("id", 2, "name", "Bob"));
        var table1 = DataTable.of(table1Rows);

        var table2Rows = List.of(
            Map.of("id", 1, "age", 25),
            Map.of("id", 2, "age", 30));
        var table2 = DataTable.of(table2Rows);

        var result = table1.join(table2, "id",
            (row1, row2) -> {
                var resultRow = new HashMap<String, Object>();
                resultRow.putAll(row1);
                resultRow.putAll(row2);
                return resultRow;
            });
        assertEquals(result.size(), 2);
        assertTrue(result.get(0).containsKey("name"));
        assertTrue(result.get(1).containsKey("name"));
        assertTrue(result.get(0).containsKey("age"));
        assertTrue(result.get(1).containsKey("age"));
        assertEquals(result.get(0).get("name"), "Alice");
        assertEquals(result.get(0).get("age"), 25);
        assertEquals(result.get(1).get("name"), "Bob");
        assertEquals(result.get(1).get("age"), 30);
    }

    @Test
    public void testSelectMultipleColumns() {
        var selectedColumnsTable = dataTable.selectColumns(List.of("Name", "Age"));
        assertEquals(selectedColumnsTable.size(), 2);
        assertFalse(selectedColumnsTable.get(0).containsKey("Country"));
        assertTrue(selectedColumnsTable.get(0).containsKey("Name"));
        assertTrue(selectedColumnsTable.get(0).containsKey("Age"));
    }

    @Test
    public void testSelectRows() {
        Map<String, Object> rowA = Map.of("ID", 1, "Name", "John Doe", "Age", 25, "IsEmployed", false);
        Map<String, Object> rowB = Map.of("ID", 2, "Name", "Jane Smith", "Age", 30, "IsEmployed", false);
        Map<String, Object> rowC = Map.of("ID", 3, "Name", "Tom", "Age", 28, "IsEmployed", false);
        var table = DataTable.of(rowA, rowB, rowC);
        var selectedRowsTable = table.selectRows(row -> (Integer) row.get("Age") > 25);
        assertEquals(selectedRowsTable.size(), 2);
        assertFalse(selectedRowsTable.contains(rowA));
        assertTrue(selectedRowsTable.contains(rowB));
        assertTrue(selectedRowsTable.contains(rowC));
    }

    @Test
    public void testJoinSum() {
        var table1Rows = List.of(
            Map.of("id", 1, "Salary", 1000),
            Map.of("id", 2, "Salary", 3000));
        var table1 = DataTable.of(table1Rows);

        var table2Rows = List.of(
            Map.of("id", 1, "Salary", 2500, "IsEmployed", true),
            Map.of("id", 2, "Salary", 3000, "IsEmployed", false));
        var table2 = DataTable.of(table2Rows);
        var joinedTable = table1.join(table2, "id",
            (firstTableRow, secondTableRow) -> {
                var joinedRow = new HashMap<String, Object>();
                int sum = firstTableRow.get("Salary") + (Integer) secondTableRow.get("Salary");
                joinedRow.putAll(firstTableRow);
                joinedRow.putAll(secondTableRow);
                joinedRow.put("Sum", sum);
                return joinedRow;
            });
        assertEquals(joinedTable.size(), 2);
        assertEquals(joinedTable.get(0).get("Sum"), 3500);
        assertEquals(joinedTable.get(1).get("Sum"), 6000);
        assertTrue(joinedTable.getColumns().contains("id"));
        assertTrue(joinedTable.getColumns().contains("Salary"));
        assertTrue(joinedTable.getColumns().contains("IsEmployed"));
        assertTrue(joinedTable.getColumns().contains("Sum"));
    }

    @Test(threadPoolSize = 3, invocationCount = 5)
    public void testAggregateByColumn() {
        var table = DataTable.of(
            Map.of("id", "1", "Amount", "9,852,855.97", "Type", "Debit"),
            Map.of("id", "2", "Amount", "9,840,000.00", "Type", "Debit"),
            Map.of("id", "3", "Amount", "120,000.00", "Type", "Credit"),
            Map.of("id", "4", "Amount", "132,855.97", "Type", "Debit"),
            Map.of("id", "5", "Amount", "19,945,711.94", "Type", "Credit"));

        var aggregatedMap = table.aggregateByColumn("Amount", "Type",
            Maths.of(BigDecimal::add));
        assertEquals(aggregatedMap.get("Debit"), "19,825,711.94");
        assertEquals(aggregatedMap.get("Credit"), "20,065,711.94");
    }

    @Test
    public void testFindingFirstAndLast() {
        var table = DataTable.of(
            Map.of("id", "1", "Amount", "9,852,855.97", "Type", "Debit"),
            Map.of("id", "2", "Amount", "9,840,000.00", "Type", "Debit"),
            Map.of("id", "3", "Amount", "120,000.00", "Type", "Credit"),
            Map.of("id", "4", "Amount", "132,855.97", "Type", "Debit"),
            Map.of("id", "5", "Amount", "19,945,711.94", "Type", "Credit"));

        table.findLast(row -> row.get("Type").equalsIgnoreCase("Debit"))
                .ifPresent(row -> assertEquals(row.get("id"), "4"));
        table.findFirst(row -> row.get("Type").equalsIgnoreCase("Credit"))
                .ifPresent(row -> assertEquals(row.get("id"), "3"));
    }

    @Test(enabled = false)
    public void testPrettyTable() {
        var table = DataTable.of(
            Map.of("name", "John Doe", "age", "30"),
            Map.of("name", "Jane Smith", "age", "25"),
            Map.of("name", "Bob Johnson", "age", "40"));
        String expectedOutput = "+-------------+-----+\n" +
                "| name        | age |\n" +
                "+-------------+-----+\n" +
                "| John Doe    | 30  |\n" +
                "| Jane Smith  | 25  |\n" +
                "| Bob Johnson | 40  |\n" +
                "+-------------+-----+\n";
        assertEquals(table.prettyTable(), expectedOutput);
    }
}
