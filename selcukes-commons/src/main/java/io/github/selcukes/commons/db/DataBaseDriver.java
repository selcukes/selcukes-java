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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Executors;

@Builder
public class DataBaseDriver {
    private DataBaseType dataBaseType;
    private String hostName;
    private String port;
    private String username;
    private String password;
    private String dataBaseName;
    private int timeout;

    private String getConnectionUrl() {
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

    private Connection createConnection() {
        Connection connection;
        var connectionUrl = getConnectionUrl();
        try {
            connection = DriverManager.getConnection(connectionUrl, username, password);
            if (!dataBaseType.toString().equals("MY_SQL") && !dataBaseType.toString().equals("POST_GRE_SQL")) {
                connection.setNetworkTimeout(Executors.newFixedThreadPool(1),
                        timeout * 60000);
            }
        } catch (Exception e) {
            throw new SelcukesException("Failed to connect DataBase using url[" + connectionUrl + "]");
        }
        return connection;
    }

    private Statement createStatement(Connection connection) {
        Statement statement;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setQueryTimeout(timeout);
        } catch (Exception e) {
            throw new SelcukesException("Failed to create a statement with this string " + connection);
        }
        return statement;
    }

    public DataBaseResult executeQuery(String query) {
        try {
            return new DataBaseResult(createStatement(createConnection()).executeQuery(query));
        } catch (Exception e) {
            throw new SelcukesException("Failed executing query " + query);
        }
    }

}
