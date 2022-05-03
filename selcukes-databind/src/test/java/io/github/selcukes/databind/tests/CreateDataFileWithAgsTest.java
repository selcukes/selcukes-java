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

package io.github.selcukes.databind.tests;

import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.annotation.DataFile;
import lombok.Data;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateDataFileWithAgsTest {
    private static final String TIMESTAMP_FORMAT = "MM/dd/yyyy hh:mm:ss";

    @Test(enabled = false)
    public void dataTest() {

        String currentDataTime = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT));
        Resolve resolve = new Resolve();
        resolve.setChromeVersion("10.11.213");
        resolve.setDataTime(currentDataTime);
        DataMapper.write(resolve);
    }

    @Data
    @DataFile(folderPath = "WebDrivers", rootFolder = "E:\\New folder")
    static class Resolve {
        String chromeVersion;
        String dataTime;
    }
}
