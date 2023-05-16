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

package io.github.selcukes.excel;

import io.github.selcukes.collections.StringHelper;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.helper.Singleton;

public class ExcelDataFactory {
    private ExcelDataFactory() {
        // Ignore this
    }

    /**
     * Returns an instance of the ExcelDataProvider implementation based on the
     * configuration specified in the application properties file.
     *
     * @return an instance of SingleExcelData or MultiExcelData, depending on
     *         the configuration
     */
    public static ExcelDataProvider getInstance() {
        String suiteFile = ConfigFactory.getConfig().getExcel().get("suiteFile");
        return StringHelper.isEmpty(suiteFile) ? Singleton.instanceOf(SingleExcelData.class)
                : Singleton.instanceOf(MultiExcelData.class);
    }
}
