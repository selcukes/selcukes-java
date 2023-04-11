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

import io.github.selcukes.commons.exception.SelcukesException;
import lombok.CustomLog;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * A class that represents a database driver for connecting to a database and
 * executing queries.
 * <p>
 * Provides methods for executing queries and updates, as well as checking if
 * the database connection is closed.
 */
@CustomLog
public class DataBaseDriver {
    private static final int TIMEOUT_SECONDS = 600;
    private Connection connection;

    /**
     * Constructs a new instance of the DataBaseDriver class with the specified
     * URL, username, and password.
     *
     * @param url      the URL of the database to connect to
     * @param username the username to use for authentication
     * @param password the password to use for authentication
     */
    public DataBaseDriver(String url, String username, String password) {
        createConnection(url, username, password);
    }

    /**
     * Creates a new connection to the database with the specified URL,
     * username, and password.
     *
     * @param  url               the URL of the database to connect to
     * @param  username          the username to use for authentication
     * @param  password          the password to use for authentication
     * @throws SelcukesException if an error occurs while connecting to the
     *                           database
     */
    private void createConnection(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new SelcukesException("Failed to connect to database using URL [" + url + "]", e);
        }
    }

    /**
     * Creates a new PreparedStatement for the specified query and sets a
     * timeout of TIMEOUT_SECONDS seconds.
     *
     * @param  query the SQL query to prepare
     * @return       a new PreparedStatement object for the specified query
     */
    @SneakyThrows
    private PreparedStatement createStatement(String query) {
        Objects.requireNonNull(connection, "Database connection is closed.");
        var statement = connection.prepareStatement(query);
        statement.setQueryTimeout(TIMEOUT_SECONDS);
        return statement;
    }

    /**
     * Executes the specified SQL query and returns a new DataBaseResult object
     * with the query results.
     *
     * @param  query             the SQL query to execute
     * @return                   a new DataBaseResult object with the query
     *                           results
     * @throws SelcukesException if an error occurs while executing the query
     */
    public DataBaseResult executeQuery(String query) {
        try {
            var resultSet = createStatement(query).executeQuery();
            return new DataBaseResult(resultSet);
        } catch (Exception e) {
            throw new SelcukesException("Failed to execute query [" + query + "]", e);
        }
    }

    /**
     * Executes the specified SQL update statement and returns the number of
     * rows affected.
     *
     * @param  query             the SQL update statement to execute
     * @return                   the number of rows affected by the update
     *                           statement
     * @throws SelcukesException if an error occurs while executing the update
     *                           statement
     */
    public int executeUpdate(String query) {
        try {
            return createStatement(query).executeUpdate();
        } catch (Exception e) {
            throw new SelcukesException("Failed to execute update [" + query + "]", e);
        }
    }

    /**
     * Closes the database connection.
     */
    @SneakyThrows
    public void closeConnection() {
        if (connection != null) {
            connection.close();
        }
    }
}
