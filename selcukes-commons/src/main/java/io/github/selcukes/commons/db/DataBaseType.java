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

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataBaseType {
    MY_SQL("jdbc:mysql://{hostName}:{port}/{dataBaseName}"),
    SQL_SERVER("jdbc:sqlserver://{hostName}:{port};databaseName={dataBaseName}{integratedSecurity}"),
    POST_GRE_SQL("jdbc:postgresql://{hostName}:{port}/{dataBaseName}"),
    ORACLE("jdbc:oracle:thin:@{hostName}:{port}:{dataBaseName}"),
    ORACLE_SERVICE_NAME("jdbc:oracle:thin:@{hostName}:{port}/{dataBaseName}"),
    IBM_DB2("jdbc:db2://{hostName}:{port}/{dataBaseName}");
    private final String url;
}
