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

/**
 * A generic data table that stores data in rows and columns.
 * <p>
 * The data table is implemented as a list of maps where each map represents a
 * row in the table,
 * <p>
 * and the keys of the maps represent the column names.
 *
 * @param <K> the type of column keys in the data table
 * @param <V> the type of column values in the data table
 */
public class DataTable<K, V> extends LinkedList<Map<K, V>> {

    /**
     * Returns a list of column keys from the first row of the data table.
     *
     * @return                       a list of column keys
     * @throws IllegalStateException if the data table is empty
     */
    public List<K> getColumns() {
        return Collections.unmodifiableList(parallelStream().findFirst()
                .map(Map::keySet)
                .map(List::copyOf)
                .orElseThrow(() -> new IllegalStateException("Data table is empty")));
    }

    /**
     * Adds a new column to the table with the given key and defaultValue.
     *
     * @param key          the key for the new column
     * @param defaultValue the defaultValue for the new column
     */
    public void addColumn(@NonNull K key, @NonNull V defaultValue) {
        updateRows(row -> {
            row.putIfAbsent(key, defaultValue);
            return row;
        });
    }

    /**
     * Adds a new row to the data table.
     *
     * @param row the map representing the new row to add
     */
    public void addRow(@NonNull Map<K, V> row) {
        add(row);
    }

    /**
     * Adds multiple rows to the data table.
     *
     * @param rows the list of maps representing the rows to add
     */
    public void addRows(@NonNull List<? extends Map<K, V>> rows) {
        addAll(rows);
    }

    /**
     * Returns the first row that matches the given predicate.
     *
     * @param  predicate the predicate to match against rows
     * @return           an {@code Optional} containing the first row that
     *                   matches the predicate, or an empty {@code Optional} if
     *                   no such row is found
     */
    public Optional<Map<K, V>> findRow(Predicate<Map<K, V>> predicate) {
        return parallelStream()
                .filter(predicate)
                .findFirst();
    }

    /**
     * Returns the first row that matches the given predicate.
     *
     * @param  predicate                the predicate to match against rows
     * @return                          the first row that matches the predicate
     * @throws IllegalArgumentException if no row matches the predicate
     */
    public Map<K, V> getRow(Predicate<Map<K, V>> predicate) {
        return findRow(predicate)
                .orElseThrow(() -> new IllegalArgumentException("Row not found for predicate: " + predicate));
    }

    /**
     * Returns an unmodifiable list of all the rows in the data table.
     *
     * @return an unmodifiable list of rows
     */
    public List<Map<K, V>> getRows() {
        return List.copyOf(this);
    }

    /**
     * Groups the rows of the DataTable based on the values of the specified
     * column key.
     *
     * @param  key                  the key of the column to group by
     * @return                      a map containing the unique column values as
     *                              keys and DataTables containing the
     *                              corresponding rows as values
     * @throws NullPointerException if key is null
     */
    public Map<V, DataTable<K, V>> groupByColumn(@NonNull K key) {
        return parallelStream().filter(map -> map.containsKey(key))
                .collect(Collectors.groupingBy(row -> row.get(key),
                    Collectors.toCollection(DataTable::new)));
    }

    /**
     * Updates each row in the table by applying the given function to the map
     * representing each row.
     *
     * @param  function             a function that takes a map representing a
     *                              row as input and returns a modified version
     *                              of the map
     * @throws NullPointerException if the function is null
     */
    public void updateRows(UnaryOperator<Map<K, V>> function) {
        replaceAll(map -> function.apply(new LinkedHashMap<>(map)));
    }

    /**
     * Updates the value in the cell at the given row index and column key.
     *
     * @param  rowIndex                  the index of the row to update
     * @param  key                       the key of the column to update
     * @param  value                     the new value for the cell
     * @throws IndexOutOfBoundsException if the row index is invalid
     */
    public void updateCell(int rowIndex, @NonNull K key, @NonNull V value) {
        checkRowIndex(rowIndex);
        var updatedRow = new LinkedHashMap<>(get(rowIndex));
        updatedRow.put(key, value);
        set(rowIndex, updatedRow);
    }

    /**
     * Returns a list of values for a given column.
     *
     * @param  columnName               the column name
     * @return                          a list of values for the given column
     * @throws IllegalArgumentException if the column name is not found in the
     *                                  table
     */
    public List<V> getColumnEntries(@NonNull K columnName) {
        checkColumnIndex(columnName);
        return parallelStream()
                .map(row -> row.getOrDefault(columnName, null))
                .collect(Collectors.toList());
    }

    /**
     * Updates the column names in the provided map using the mapping specified
     * in the {@code columnMapping} parameter.
     *
     * @param columnMapping a map containing the old column names as keys and
     *                      the corresponding new column names as values
     */
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

    /**
     * Gets the cell value at the specified row and column in the DataTable.
     *
     * @param  rowIndex   the index of the row to get the cell value from
     * @param  columnName the name of the column to get the cell value from
     * @return            the cell value at the specified row and column
     */
    public V getCellValue(int rowIndex, @NonNull K columnName) {
        checkRowIndex(rowIndex);
        return get(rowIndex).get(columnName);
    }

    /**
     * Returns true if the data table contains the expected row.
     *
     * @param  expectedRow          the map representing the expected row
     * @return                      true if the data table contains the expected
     *                              row, false otherwise
     * @throws NullPointerException if the expected row is null
     */
    public boolean contains(@NonNull Map<K, V> expectedRow) {
        return parallelStream().anyMatch(actualRow -> expectedRow.entrySet().containsAll(actualRow.entrySet()));
    }

    /**
     * Returns a new DataTable instance initialized with the data provided as a
     * List of Map objects.
     *
     * @param  dataList             a list of Maps representing the data rows to
     *                              add to the new DataTable instance
     * @param  <K>                  the type of the column keys
     * @param  <V>                  the type of the column values
     * @return                      a new DataTable instance
     * @throws NullPointerException if dataList is null
     */
    public static <K, V> DataTable<K, V> of(@NonNull List<? extends Map<K, V>> dataList) {
        var dataTable = new DataTable<K, V>();
        dataTable.addRows(dataList);
        return dataTable;
    }

    /**
     * Creates a new DataTable from a list of Maps.
     *
     * @param  elements a variable number of Maps containing key-value pairs to
     *                  be added to the DataTable
     * @return          a new DataTable object containing the key-value pairs
     *                  from the input Maps
     */
    @SafeVarargs
    public static <K, V> DataTable<K, V> of(Map<K, V>... elements) {
        return of(List.of(elements));
    }

    /**
     * Removes rows from the DataTable that match the given predicate.
     *
     * @param predicate the predicate to use for filtering the rows to remove
     */
    public void removeRows(Predicate<Map<K, V>> predicate) {
        removeIf(predicate);
    }

    /**
     * Removes the row at the specified index from the DataTable.
     *
     * @param index The index of the row to remove
     */
    public void removeRow(int index) {
        checkRowIndex(index);
        remove(index);
    }

    /**
     * Sorts the rows in the table by the values in the column with the given
     * columnName, using the given comparator to determine the order.
     *
     * @param  columnName               the name of the column to sort by
     * @param  comparator               a comparator to determine the order of
     *                                  the values
     * @throws IllegalArgumentException if the column columnName is not found in
     *                                  the table
     */
    public void sortByColumn(@NonNull K columnName, Comparator<V> comparator) {
        checkColumnIndex(columnName);
        sort(Comparator.comparing(row -> row.get(columnName), comparator));
    }

    private void checkRowIndex(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= size()) {
            throw new IndexOutOfBoundsException("Invalid row index: " + rowIndex);
        }
    }

    private void checkColumnIndex(K columnName) {
        var columnIndex = getColumns().indexOf(columnName);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
    }

    /**
     * Returns a new DataTable with only the selected columns.
     *
     * @param  columns The list of column names to select.
     * @return         A new DataTable with only the selected columns.
     */
    public DataTable<K, V> selectColumns(@NonNull List<K> columns) {
        return parallelStream()
                .map(row -> row.entrySet().stream()
                        .filter(entry -> columns.contains(entry.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .collect(DataTable::new, DataTable::addRow, DataTable::addAll);
    }

    /**
     * Joins the current DataTable with another DataTable on the specified join
     * column and returns a new DataTable with the combined rows.
     *
     * @param  <R>          The type of the value for the joined table.
     * @param  <L>          The type of the value for the other table.
     * @param  otherTable   The DataTable to join with.
     * @param  joinColumn   The column name to join on.
     * @param  joinFunction The function to apply to each matching row pair.
     * @return              A new DataTable with the combined rows.
     */
    public <R, L> DataTable<K, R> join(
            @NonNull DataTable<K, L> otherTable, @NonNull K joinColumn,
            @NonNull BiFunction<Map<K, V>, Map<K, L>, Map<K, R>> joinFunction
    ) {
        return parallelStream()
                .flatMap(row -> otherTable.getRows().stream()
                        .filter(otherRow -> row.get(joinColumn).equals(otherRow.get(joinColumn)))
                        .map(matchingRow -> joinFunction.apply(row, matchingRow)))
                .collect(Collectors.toCollection(DataTable::new));
    }

    /**
     * Returns a string representation of the DataTable.
     *
     * @return a string representation of the DataTable
     */
    @Override
    public String toString() {
        StringJoiner table = new StringJoiner("\n");
        table.add(getColumns().toString());
        forEach(row -> table.add(row.values().toString()));
        return table.toString();
    }
}
