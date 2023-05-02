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

import io.github.selcukes.databind.annotation.DataFile;
import io.github.selcukes.databind.annotation.Interpolate;
import io.github.selcukes.databind.annotation.Key;
import io.github.selcukes.databind.properties.PropertiesMapper;
import io.github.selcukes.databind.substitute.StringSubstitutor;
import io.github.selcukes.databind.utils.Clocks;
import io.github.selcukes.databind.utils.Resources;
import lombok.Data;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class PropertiesWriterTest {
    @Interpolate(substitutor = StringSubstitutor.class)
    @DataFile(fileName = "test_config.properties", folderPath = "${dataFile.location}")
    @Data
    private static class TestConfig {
        String userName;
        String password;
        boolean isTest;
        String osName;
        LocalDate date;
        @Key(name = "sampleDate", format = "MM/dd/yyyy")
        LocalDate sampleDate;
        @Key(name = "test.count")
        int count;
        @Key(name = "elements", converter = ListStringConverter.class)
        List<String> elements;
    }

    @Test
    public void propsWriter() {
        System.setProperty("dataFile.location", "");
        var config = PropertiesMapper.parse(TestConfig.class);
        System.setProperty("dataFile.location", "target");
        PropertiesMapper.write(config);
        assertTrue(Files.exists(Resources.of("target/test_config.properties")));
        var config1 = PropertiesMapper.parse(TestConfig.class);
        assertEquals(config1.getDate(), Clocks.dateNow());
        assertEquals(config1.getElements(), List.of("ele1", "ele2"));
    }
}
