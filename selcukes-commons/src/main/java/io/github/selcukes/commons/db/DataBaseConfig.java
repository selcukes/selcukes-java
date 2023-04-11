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

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DataBaseConfig {
    private final DataBaseType dataBaseType;
    private final String hostName;
    private final int port;
    private final String dataBaseName;
    private final String userName;
    private final String password;
    @Builder.Default
    private final int timeout = 600;
    private final boolean integratedSecurity;

    /**
     * Returns the URL to connect to the database based on the configuration
     * properties.
     *
     * @return the URL to connect to the database
     */
    public String getUrl() {
        String url = dataBaseType.getUrl()
                .replace("{hostName}", hostName)
                .replace("{port}", String.valueOf(port))
                .replace("{dataBaseName}", dataBaseName);
        if (integratedSecurity) {
            url += ";integratedSecurity=true";
        }
        return url;
    }
}
