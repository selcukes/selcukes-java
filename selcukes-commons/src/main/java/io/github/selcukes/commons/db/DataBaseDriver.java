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

import io.github.selcukes.commons.exception.DriverConnectionException;
import io.github.selcukes.databind.collections.DataTable;
import lombok.CustomLog;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * A class that represents a database driver for connecting to a database and
 * executing queries.
 * <p>
 * Provides methods for executing queries and updates, as well as checking if
 * the database connection is closed.
 */
@CustomLog
public class DataBaseDriver {
    private Connection connection;
    private final int timeout;

    /**
     * Constructs a new instance of the DataBaseDriver class with the specified
     * database configuration.
     *
     * @param dataBaseConfig the configuration of the database to connect to
     */
    public DataBaseDriver(DataBaseConfig dataBaseConfig) {
        this.timeout = dataBaseConfig.getTimeout();
        createConnection(dataBaseConfig.getUrl(), dataBaseConfig.getUserName(), dataBaseConfig.getPassword());
    }

    /**
     * Creates a new connection to the database with the specified URL,
     * username, and password.
     *
     * @param  url                       the URL of the database to connect to
     * @param  username                  the username to use for authentication
     * @param  password                  the password to use for authentication
     * @throws DriverConnectionException if an error occurs while connecting to
     *                                   the database
     */
    private void createConnection(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new DriverConnectionException("Failed to connect to database using URL [" + url + "]", e);
        }
    }

    /**
     * Creates a new {@code Statement} object for the specified SQL query and
     * sets a timeout of seconds.
     *
     * @return a new {@code Statement} object for the specified query
     */
    private synchronized Statement createStatement() {
        Statement statement;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setQueryTimeout(timeout);
        } catch (Exception e) {
            throw new DriverConnectionException("Failed to create a statement with this connection: " + connection
                    + ". Error message: " + e.getMessage());

        }
        return statement;
    }

    /**
     * Executes a query on the connected database and returns a{@link DataTable}
     *
     * @param  query                     the SQL query to execute
     * @return                           return a new {@link DataTable} object
     *                                   with the query results
     * @throws DriverConnectionException if an error occurs while executing the
     *                                   query
     */
    public synchronized DataTable<String, String> executeQuery(String query) {
        try (var statement = createStatement();
                var resultSet = statement.executeQuery(query)) {
            logger.debug(() -> String.format("Executed query [%s]", query));
            return asTable(resultSet);
        } catch (Exception e) {
            throw new DriverConnectionException("Failed to execute query [" + query + "]", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Executes the specified SQL update statement and returns the number of
     * rows affected.
     *
     * @param  query                     the SQL update statement to execute
     * @return                           the number of rows affected by the
     *                                   update statement
     * @throws DriverConnectionException if an error occurs while executing the
     *                                   update statement
     */
    public synchronized int executeUpdate(String query) {
        try (var statement = createStatement()) {
            logger.debug(() -> String.format("Executing Update [%s]", query));
            return statement.executeUpdate(query);
        } catch (Exception e) {
            throw new DriverConnectionException("Failed to execute update [" + query + "]", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * Sets the network timeout for the database connection.
     *
     * @param  seconds                   the timeout value in seconds
     * @return                           this instance of {@code DataBaseDriver}
     * @throws DriverConnectionException if there is an error setting the
     *                                   timeout
     */
    public synchronized DataBaseDriver setNetworkTimeout(int seconds) {
        try {
            connection.setNetworkTimeout(Executors.newFixedThreadPool(1), seconds * 1000);
        } catch (SQLException e) {
            throw new DriverConnectionException("Failed to set timeout on database connection", e);
        }
        return this;
    }

    /**
     * Closes the database connection.
     */
    @SneakyThrows
    private void closeConnection() {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Converts the underlying ResultSet to a {@link DataTable}.
     *
     * @param  resultSet the ResultSet object to create the DataTable from.
     * @return           a {@code DataTable} object containing the data from the
     *                   ResultSet.
     */
    private DataTable<String, String> asTable(ResultSet resultSet) throws SQLException {
        var table = new DataTable<String, String>();
        var meta = resultSet.getMetaData();
        while (resultSet.next()) {
            table.addRow(asRow(meta, resultSet));
        }
        return table;
    }

    /**
     * Converts a row of the underlying ResultSet to a {@link Map} with column
     * names as keys and column values as values.
     *
     * @param  metaData  the metadata for the ResultSet.
     * @param  resultSet the ResultSet to be converted.
     * @return           a {@code Map} object representing the row.
     */
    private Map<String, String> asRow(final ResultSetMetaData metaData, final ResultSet resultSet) throws SQLException {
        var map = new LinkedHashMap<String, String>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            map.put(metaData.getColumnName(i), resultSet.getString(i));
        }
        return Collections.unmodifiableMap(map);
    }
}
