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
import lombok.Builder;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Executors;

import static java.util.Optional.ofNullable;

/**
 * A class representing a driver for a database.
 * <p>
 * This class allows executing queries on different types of databases.
 */
@Getter
@Builder
public class DataBaseDriver {
    /**
     * The type of database to connect to.
     */
    private DataBaseType dataBaseType;
    /**
     * The hostname or IP address of the database server.
     */
    private String hostName;
    /**
     * The port number to connect to the database.
     */
    private String port;
    /**
     * The username used to authenticate the connection.
     */
    private String username;
    /**
     * The password used to authenticate the connection.
     */
    private String password;
    /**
     * The name of the database to connect to.
     */
    private String dataBaseName;
    /**
     * The timeout to be used for the connection and statement in minutes.
     */
    private int timeout;
    /**
     * The connection URL used to connect to the database.
     */
    private String connectionUrl;

    /**
     * Constructs a connection URL based on the specified database type, host
     * name, port, and database name.
     *
     * @return                   the connection URL as a string.
     * @throws SelcukesException if an invalid database type is specified.
     */
    private String getDefaultConnectionUrl() {
        switch (dataBaseType) {
            case MY_SQL:
                return "jdbc:mysql://" + hostName + ":" + port + "/" + dataBaseName;
            case SQL_SERVER:
                return "jdbc:sqlserver://" + hostName + ":" + port + ";databaseName=" + dataBaseName;
            case POST_GRE_SQL:
                return "jdbc:postgresql://" + hostName + ":" + port + "/" + dataBaseName;
            case ORACLE:
                return "jdbc:oracle:thin:@" + hostName + ":" + port + ":" + dataBaseName;
            case ORACLE_SERVICE_NAME:
                return "jdbc:oracle:thin:@" + hostName + ":" + port + "/" + dataBaseName;
            case IBM_DB2:
                return "jdbc:db2://" + hostName + ":" + port + "/" + dataBaseName;
            default: {
                throw new SelcukesException("Database not supported");

            }
        }
    }

    /**
     * Creates a connection to the database using the specified connection URL,
     * username, and password.
     *
     * @return                   a {@link Connection} object.
     * @throws SelcukesException if an error occurs while creating the
     *                           connection.
     */
    private Connection createConnection() {
        Connection connection;
        var dbUrl = ofNullable(getConnectionUrl()).orElse(getDefaultConnectionUrl());
        try {
            connection = DriverManager.getConnection(dbUrl, username, password);
            if (!dataBaseType.toString().equals("MY_SQL") && !dataBaseType.toString().equals("POST_GRE_SQL")) {
                connection.setNetworkTimeout(Executors.newFixedThreadPool(1),
                    timeout * 60000);
            }
        } catch (Exception e) {
            throw new SelcukesException("Failed to connect DataBase using url[" + dbUrl + "]");
        }
        return connection;
    }

    /**
     * Creates a {@link Statement} object for executing SQL queries on the
     * specified {@link Connection} object.
     *
     * @param  connection        the {@link Connection} object to create the
     *                           statement on.
     * @return                   a {@link Statement} object for executing SQL
     *                           queries.
     * @throws SelcukesException if there is an error creating the
     *                           {@link Statement}.
     */
    private Statement createStatement(final Connection connection) {
        Statement statement;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setQueryTimeout(timeout);
        } catch (Exception e) {
            throw new SelcukesException("Failed to create a statement with this string " + connection);
        }
        return statement;
    }

    /**
     * Executes a query on the database and returns a {@link DataBaseResult}
     * object.
     *
     * @param  query             the query to be executed.
     * @return                   a {@link DataBaseResult} object.
     * @throws SelcukesException if an error occurs while executing the query.
     */
    public DataBaseResult executeQuery(final String query) {
        try (var connection = createConnection();
                var statement = createStatement(connection);
                var resultSet = statement.executeQuery(query)) {
            return new DataBaseResult(resultSet);
        } catch (Exception e) {
            throw new SelcukesException("Failed executing query " + query, e);
        }
    }

}
