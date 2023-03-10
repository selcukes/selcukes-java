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

package io.github.selcukes.databind.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A generic data table that stores data in the form of rows and columns, where
 * each row is represented by a map of column names to values.
 *
 * @param <T> the type of the column values
 */
public class DataTable<T> {
    private final List<Map<String, T>> rows;
    private final List<String> columns;

    /**
     * Constructs a new DataTable with the given column names.
     *
     * @param  columns              the names of the columns
     * @throws NullPointerException if columns is null
     */
    public DataTable(List<String> columns) {
        this.columns = Objects.requireNonNull(columns, "Columns cannot be null");
        rows = new ArrayList<>();
    }

    /**
     * Adds a new row to the DataTable.
     *
     * @param row the row to add
     */
    public synchronized void addRow(Map<String, T> row) {
        rows.add(Map.copyOf(row));
    }

    /**
     * Adds multiple rows to the DataTable.
     *
     * @param rows the rows to add
     */
    public synchronized void addRows(List<Map<String, T>> rows) {
        this.rows.addAll(List.copyOf(rows));
    }

    /**
     * Returns a copy of the rows in the DataTable.
     *
     * @return a copy of the rows
     */
    public synchronized List<Map<String, T>> getRows() {
        return List.copyOf(rows);
    }

    /**
     * Returns the first row in the DataTable that satisfies the given
     * predicate.
     *
     * @param  predicate                the predicate to test each row
     * @return                          the first row that satisfies the
     *                                  predicate
     * @throws IllegalArgumentException if no row satisfies the predicate
     */
    public synchronized Map<String, T> getRow(Predicate<Map<String, T>> predicate) {
        return rows.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Row not found for predicate: " + predicate));
    }

    /**
     * Groups the rows in the DataTable by the value of the given key column.
     *
     * @param  key the name of the key column
     * @return     a map of key column values to lists of rows with that value
     */
    public synchronized Map<T, List<Map<String, T>>> getRowsGroupedByColumn(String key) {
        return getRows().stream()
                .filter(map -> map.containsKey(key))
                .collect(Collectors.groupingBy(map -> map.get(key),
                    Collectors.collectingAndThen(Collectors.toList(),
                        Collections::unmodifiableList)));
    }

    /**
     * Returns a copy of the column names in the DataTable.
     *
     * @return a copy of the column names
     */
    public synchronized List<String> getColumns() {
        return List.copyOf(columns);
    }

    /**
     * Returns a copy of the values in the given column.
     *
     * @param  columnName               the name of the column to get values
     *                                  from
     * @return                          a copy of the column values
     * @throws IllegalArgumentException if the column does not exist in the
     *                                  DataTable
     */
    public synchronized List<T> getColumnValues(String columnName) {
        int columnIndex = columns.indexOf(columnName);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return List.copyOf(rows).stream()
                .map(row -> row.getOrDefault(columnName, null))
                .collect(Collectors.toList());
    }

    /**
     * Determines whether the DataTable contains a row that has the same values
     * as the given row.
     *
     * @param  expectedRow          the row to check for
     * @return                      true if the DataTable contains the expected
     *                              row, false otherwise
     * @throws NullPointerException if the expectedRow is null
     */
    public synchronized boolean contains(Map<String, T> expectedRow) {
        Objects.requireNonNull(expectedRow, "Expected row cannot be null");
        return rows.stream().anyMatch(actualRow -> expectedRow.entrySet().containsAll(actualRow.entrySet()));
    }

    /**
     * Constructs a new DataTable from the given list of maps. The method
     * creates a new DataTable instance with the columns derived from the keys
     * of the first map in the dataList. It then populates the DataTable with
     * rows from the maps in the dataList.
     *
     * @param  dataList             the list of maps representing the data to be
     *                              used to populate the DataTable
     * @param  <T>                  the data type of the values in the maps
     * @return                      the new DataTable instance
     * @throws NullPointerException if the dataList is null
     */
    public static <T> DataTable<T> of(List<Map<String, T>> dataList) {
        Objects.requireNonNull(dataList, "Data list cannot be null");
        var columns = List.copyOf(dataList.get(0).keySet());
        var dataTable = new DataTable<T>(columns);
        dataTable.addRows(dataList);
        return dataTable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DataTable)) {
            return false;
        }
        DataTable<?> other = (DataTable<?>) obj;
        return Objects.equals(columns, other.columns) && Objects.equals(rows, other.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, rows);
    }

    @Override
    public String toString() {
        return "DataTable{" +
                "rows=" + rows +
                ", columns=" + columns +
                '}';
    }
}
