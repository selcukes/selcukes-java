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

package io.github.selcukes.commons.db;

import io.github.selcukes.databind.collections.DataTable;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The {@code DataBaseResult} class is a wrapper around a JDBC
 * {@link ResultSet}, providing a method to convert it to a {@link DataTable}.
 * <p>
 * It can be used to represent the result set of a database query as a
 * table-like structure with named columns and rows.
 * <p>
 * Usage:
 *
 * <pre>
 * {@code
 * // Obtain a ResultSet from a JDBC connection
 *
 * ResultSet resultSet = ...
 *
 * // Wrap the ResultSet in a DataBaseResult object
 *
 * DataBaseResult dataBaseResult = new DataBaseResult(resultSet);
 *
 * // Convert the result set to a DataTable
 *
 * DataTable<String, Object> dataTable = dataBaseResult.asTable();
 *
 * }
 * </pre>
 * <p>
 */
public class DataBaseResult {
    /**
     * The underlying JDBC ResultSet.
     */
    private final ResultSet resultSet;

    /**
     * Constructs a new {@code DataBaseResult} object with the specified JDBC
     * ResultSet.
     *
     * @param resultSet the ResultSet to be wrapped.
     */
    public DataBaseResult(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * Converts the underlying ResultSet to a {@link DataTable}.
     *
     * @return a {@code DataTable} object representing the result set as a
     *         table.
     */
    @SneakyThrows
    public DataTable<String, String> asTable() {
        var table = new DataTable<String, String>();
        try (resultSet) {
            var meta = resultSet.getMetaData();
            while (resultSet.next()) {
                table.addRow(asRow(meta, resultSet));
            }
        }
        return table;
    }

    /**
     * Returns the underlying ResultSet.
     *
     * @return the ResultSet wrapped by this object.
     */
    public ResultSet getResultSet() {
        return resultSet;
    }

    /**
     * Converts a row of the underlying ResultSet to a {@link Map} with column
     * names as keys and column values as values.
     *
     * @param  metaData  the metadata for the ResultSet.
     * @param  resultSet the ResultSet to be converted.
     * @return           a {@code Map} object representing the row.
     */
    @SneakyThrows
    private Map<String, String> asRow(final ResultSetMetaData metaData, final ResultSet resultSet) {
        var map = new LinkedHashMap<String, String>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            map.put(metaData.getColumnName(i), resultSet.getString(i));
        }
        return Collections.unmodifiableMap(map);
    }
}
