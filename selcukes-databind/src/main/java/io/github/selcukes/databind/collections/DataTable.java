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

import lombok.Getter;
import lombok.NonNull;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
@Getter
public class DataTable<K, V> extends LinkedList<Map<K, V>> {

    /**
     * Returns a list of column keys from the first row of the data table.
     *
     * @return                       a list of column keys
     * @throws IllegalStateException if the data table is empty
     */
    public List<K> getColumns() {
        if (isEmpty()) {
            throw new IllegalStateException("Data table is empty");
        }
        return List.copyOf(get(0).keySet());
    }

    /**
     * Adds a new column to the table with the given key and defaultValue.
     *
     * @param key          the key for the new column
     * @param defaultValue the defaultValue for the new column
     */
    public void addColumn(@NonNull K key, @NonNull V defaultValue) {

        var updatedRows = stream()
                .map(row -> {
                    var updatedRow = new LinkedHashMap<>(row);
                    updatedRow.putIfAbsent(key, defaultValue);
                    return updatedRow;
                })
                .collect(Collectors.toList());
        clear();
        addAll(updatedRows);
    }

    /**
     * Adds a new row to the data table.
     *
     * @param row the map representing the new row to add
     */
    public void addRow(@NonNull Map<K, V> row) {
        add(Map.copyOf(row));
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
     * @param  predicate                the predicate to match against rows
     * @return                          the first row that matches the predicate
     * @throws IllegalArgumentException if no row matches the predicate
     */
    public Map<K, V> getRow(Predicate<Map<K, V>> predicate) {
        return stream()
                .filter(predicate)
                .findFirst()
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
    public Map<V, DataTable<K, V>> groupByColumnValues(@NonNull K key) {
        return stream().filter(map -> map.containsKey(key))
                .collect(Collectors.groupingBy(map -> map.get(key),
                    Collectors.collectingAndThen(Collectors.toList(),
                        DataTable::of)));
    }

    /**
     * Updates each row in the data table with the result of applying the given
     * function to the row.
     *
     * @param function the function to apply to each row
     */
    public void updateRows(UnaryOperator<Map<K, V>> function) {
        replaceAll(function);
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
    public List<V> getColumnValues(@NonNull K columnName) {
        checkColumnIndex(columnName);
        return List.copyOf(this).stream()
                .map(row -> row.getOrDefault(columnName, null))
                .collect(Collectors.toList());
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
        return stream().anyMatch(actualRow -> expectedRow.entrySet().containsAll(actualRow.entrySet()));
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
        sort((row1, row2) -> comparator.compare(row1.get(columnName), row2.get(columnName)));
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
     * Selects the specified columns from the current DataTable and returns a
     * new DataTable with only the selected columns.
     *
     * @param  columns The list of column names to select.
     * @return         A new DataTable with only the selected columns.
     */
    public DataTable<K, V> select(@NonNull List<K> columns) {
        var selectedColumns = new DataTable<K, V>();
        selectedColumns.addAll(this);
        selectedColumns.replaceAll(row -> columns.stream()
                .collect(Maps.of(column -> column, row::get)));
        return selectedColumns;
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
            DataTable<K, L> otherTable, K joinColumn, BiFunction<Map<K, V>, Map<K, L>, Map<K, R>> joinFunction
    ) {
        var joinedTable = new DataTable<K, R>();
        this.forEach(row -> {
            var matchingRows = otherTable.getRows().stream()
                    .filter(otherRow -> row.get(joinColumn).equals(otherRow.get(joinColumn)))
                    .collect(Collectors.toList());
            matchingRows.forEach(matchingRow -> {
                var joinedRow = joinFunction.apply(row, matchingRow);
                joinedTable.addRow(joinedRow);
            });
        });
        return joinedTable;
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
