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

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * It wraps a JDBC ResultSet and provides a method to convert it to a List of Maps
 */
public class DataBaseResult {
    private final ResultSet resultSet;

    public DataBaseResult(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }


    /**
     * It takes a ResultSet and returns a List of Maps, where each Map represents a row in the ResultSet
     *
     * @return A list of maps.
     */
    @SneakyThrows
    public List<Map<String, Object>> asList() {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData meta = resultSet.getMetaData();
        while (resultSet.next()) {
            list.add(asRow(meta, resultSet));
        }
        return list;
    }

    /**
     * This function returns the result set.
     *
     * @return The resultSet is being returned.
     */
    public ResultSet getResultSet() {
        return resultSet;
    }

    @SneakyThrows
    private Map<String, Object> asRow(final ResultSetMetaData metaData, final ResultSet resultSet) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            map.put(metaData.getColumnName(i), resultSet.getString(i));
        }
        return Collections.unmodifiableMap(map);
    }

}
