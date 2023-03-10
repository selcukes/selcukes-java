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

public class DataTable<T> {
    private final List<Map<String, T>> rows;
    private final List<String> columns;

    public DataTable(List<String> columns) {
        this.columns = Objects.requireNonNull(columns, "Columns cannot be null");
        rows = new ArrayList<>();
    }

    public synchronized void addRow(Map<String, T> row) {
        rows.add(Map.copyOf(row));
    }

    public synchronized void addRows(List<Map<String, T>> rows) {
        this.rows.addAll(List.copyOf(rows));
    }

    public synchronized List<Map<String, T>> getRows() {
        return List.copyOf(rows);
    }

    public synchronized Map<String, T> getRow(Predicate<Map<String, T>> predicate) {
        return rows.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Row not found for predicate: " + predicate));
    }

    public synchronized Map<T, List<Map<String, T>>> getRowsGroupedByColumn(String key) {
        return getRows().stream()
                .filter(map -> map.containsKey(key))
                .collect(Collectors.groupingBy(map -> map.get(key),
                        Collectors.collectingAndThen(Collectors.toList(),
                                Collections::unmodifiableList)));
    }

    public synchronized List<String> getColumns() {
        return List.copyOf(columns);
    }

    public synchronized List<T> getColumnValues(String columnName) {
        int columnIndex = columns.indexOf(columnName);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return List.copyOf(rows).stream()
                .map(row -> row.getOrDefault(columnName, null))
                .collect(Collectors.toList());
    }

    public synchronized boolean contains(Map<String, T> expectedRow) {
        return rows.stream().anyMatch(actualRow -> expectedRow.entrySet().containsAll(actualRow.entrySet()));
    }

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

