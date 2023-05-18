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

package io.github.selcukes.collections.tests;

import io.github.selcukes.collections.DataComparator;
import io.github.selcukes.collections.DataTable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DataComparatorTest {
    private DataTable<String, String> expected;
    private DataTable<String, String> actual;

    @BeforeMethod
    public void setup() {
        expected = DataTable.of(
            Map.of("id", "1", "Name", "Alice", "Amount", "120,000.00", "Type", "Credit"),
            Map.of("id", "2", "Name", "Bob", "Amount", "132,855.97", "Type", "Debit"),
            Map.of("id", "3", "Name", "Charlie", "Amount", "132,855.97", "Type", "Error1"),
            Map.of("id", "4", "Name", "Dave", "Amount", "19,945,711.94", "Type", "Credit"));
        actual = DataTable.of(
            Map.of("id", "1", "Name", "Alice", "Amount", "120,000.00", "Type", "Credit"),
            Map.of("id", "2", "Name", "Bob", "Amount", "132,855.97", "Type", "Debit"),
            Map.of("id", "3", "Name", "Charlie", "Amount", "132,855.97", "Type", "Error"),
            Map.of("id", "4", "Name", "Dave", "Amount", "19,945,711.94", "Type", "Credit"));
    }

    @Test
    public void testCheckTableData() {
        var differences = DataComparator.diff(expected, actual, "id", List.of("Type"));
        assertEquals(differences.size(), 12);
        assertFalse(isFailed(differences));
        var differences1 = DataComparator.diff(expected, actual, "id");
        assertEquals(differences.size(), 12);
        assertTrue(isFailed(differences1));
        System.out.println(differences1.prettyTable());
    }

    @Test
    public void testCheckRowData() {
        var expectedRow = Map.of("id", "1", "Name", "Alice", "Amount", "120,000.00", "Type", "Credit");
        var actualRow = Map.of("id", "1", "Name", "Bob", "Amount", "120,000.00", "Type", "Credit");

        var differences = DataComparator.diff(expectedRow, actualRow);
        assertTrue(isFailed(differences));
        System.out.println(differences.prettyTable());
    }

    @Test
    public void testCheckColumnData() {
        var expectedColumn = expected.getColumnEntries("Name");
        var actualColumn = actual.getColumnEntries("Name");

        var differences = DataComparator.diff(expectedColumn, actualColumn);
        assertFalse(isFailed(differences));
        assertEquals(differences.size(), 4);
        System.out.println(differences.prettyTable());
        expectedColumn.add("Hello");
        actualColumn.add(null);
        var differences1 = DataComparator.diff(expectedColumn, actualColumn);
        assertTrue(isFailed(differences1));
        assertEquals(differences1.size(), 5);
        System.out.println(differences1.prettyTable());
    }

    private boolean isFailed(DataTable<String, String> result) {
        return result.isRowExists("Status", "Fail");
    }
}
