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

package io.github.selcukes.databind.collections;

import io.github.selcukes.databind.exception.DataTableException;
import lombok.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataTable<K, V> extends LinkedList<Map<K, V>> {

    public List<K> getColumns() {
        return Collections.unmodifiableList(parallelStream().findFirst()
                .map(Map::keySet)
                .map(List::copyOf)
                .orElseThrow(() -> new DataTableException("Data table is empty, cannot retrieve column names")));
    }

    public void addColumn(@NonNull K key, @NonNull V defaultValue) {
        updateRows(row -> {
            row.putIfAbsent(key, defaultValue);
            return row;
        });
    }

    public void addRow(@NonNull Map<K, V> row) {
        add(row);
    }

    public void addRows(@NonNull List<? extends Map<K, V>> rows) {
        addAll(rows);
    }

    public Optional<Map<K, V>> findRow(Predicate<Map<K, V>> predicate) {
        return rows()
                .filter(predicate)
                .findFirst();
    }

    public Stream<Map<K, V>> rows() {
        return parallelStream();
    }

    public Map<V, DataTable<K, V>> groupByColumn(@NonNull K key) {
        return rows().filter(map -> map.containsKey(key))
                .collect(Collectors.groupingBy(row -> row.get(key),
                    Collectors.toCollection(DataTable::new)));
    }

    public void updateRows(UnaryOperator<Map<K, V>> function) {
        replaceAll(map -> function.apply(new LinkedHashMap<>(map)));
    }

    public void updateCell(int rowIndex, @NonNull K key, @NonNull V value) {
        checkRowIndex(rowIndex);
        var updatedRow = new LinkedHashMap<>(get(rowIndex));
        updatedRow.put(key, value);
        set(rowIndex, updatedRow);
    }

    public List<V> getColumnEntries(@NonNull K columnName) {
        checkColumnIndex(columnName);
        return rows()
                .map(row -> row.getOrDefault(columnName, null))
                .collect(Collectors.toList());
    }

    public void renameColumn(Map<K, K> columnMapping) {
        updateRows(row -> {
            columnMapping.forEach((oldKey, newKey) -> {
                if (row.containsKey(oldKey)) {
                    row.put(newKey, row.remove(oldKey));
                }
            });
            return row;
        });
    }

    public V getCellValue(int rowIndex, @NonNull K columnName) {
        checkRowIndex(rowIndex);
        return get(rowIndex).get(columnName);
    }

    public boolean contains(@NonNull Map<K, V> expectedRow) {
        return rows().anyMatch(actualRow -> expectedRow.entrySet().containsAll(actualRow.entrySet()));
    }

    public static <K, V> DataTable<K, V> of(@NonNull List<? extends Map<K, V>> dataList) {
        var dataTable = new DataTable<K, V>();
        dataTable.addRows(dataList);
        return dataTable;
    }

    @SafeVarargs
    public static <K, V> DataTable<K, V> of(Map<K, V>... elements) {
        return of(List.of(elements));
    }

    public void removeRows(Predicate<Map<K, V>> predicate) {
        removeIf(predicate);
    }

    public void removeRow(int index) {
        checkRowIndex(index);
        remove(index);
    }

    public void sortByColumn(@NonNull K columnName, Comparator<V> comparator) {
        checkColumnIndex(columnName);
        sort(Comparator.comparing(row -> row.get(columnName), comparator));
    }

    private void checkRowIndex(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= size()) {
            throw new DataTableException("Invalid row index: " + rowIndex);
        }
    }

    private void checkColumnIndex(K columnName) {
        var columnIndex = getColumns().indexOf(columnName);
        if (columnIndex < 0) {
            throw new DataTableException("Column not found: " + columnName);
        }
    }

    public DataTable<K, V> selectColumns(@NonNull List<K> columns) {
        return rows()
                .map(row -> row.entrySet().stream()
                        .filter(entry -> columns.contains(entry.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .collect(DataTable::new, DataTable::addRow, DataTable::addAll);
    }

    public DataTable<K, V> selectRows(Predicate<Map<K, V>> predicate) {
        return rows()
                .filter(predicate)
                .collect(DataTable::new, DataTable::addRow, DataTable::addAll);
    }

    public <R, L> DataTable<K, R> join(
            @NonNull DataTable<K, L> otherTable, @NonNull K joinColumn,
            @NonNull BiFunction<Map<K, V>, Map<K, L>, Map<K, R>> joinFunction
    ) {
        return rows()
                .flatMap(row -> otherTable.rows()
                        .filter(otherRow -> row.get(joinColumn).equals(otherRow.get(joinColumn)))
                        .map(matchingRow -> joinFunction.apply(row, matchingRow)))
                .collect(Collectors.toCollection(DataTable::new));
    }

    @Override
    public String toString() {
        StringJoiner table = new StringJoiner("\n");
        table.add(getColumns().toString());
        forEach(row -> table.add(row.values().toString()));
        return table.toString();
    }
}
