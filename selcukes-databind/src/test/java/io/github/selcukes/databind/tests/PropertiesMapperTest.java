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

import io.github.selcukes.collections.Clocks;
import io.github.selcukes.databind.annotation.DataFile;
import io.github.selcukes.databind.annotation.Interpolate;
import io.github.selcukes.databind.annotation.Key;
import io.github.selcukes.databind.properties.PropertiesMapper;
import io.github.selcukes.databind.substitute.StringSubstitutor;
import lombok.Data;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;
import java.util.List;

public class PropertiesMapperTest {

    @Test
    public void testProperties() {
        var testConfig = PropertiesMapper.parse(TestConfig.class);
        var softAssert = new SoftAssert();
        softAssert.assertEquals(testConfig.getUserName(), "Ramesh");
        softAssert.assertEquals(testConfig.getPassword(), "cred");
        softAssert.assertTrue(testConfig.isTest());
        softAssert.assertEquals(testConfig.getOsName(), System.getProperty("os.name"));
        softAssert.assertEquals(testConfig.getDate(), Clocks.dateNow());
        softAssert.assertEquals(testConfig.getSampleDate(), Clocks.parseDate("12/12/2022", ""));
        softAssert.assertEquals(testConfig.getElements(), List.of("ele1", "ele2"));
        softAssert.assertAll();
    }

    @Interpolate(substitutor = StringSubstitutor.class)
    @DataFile
    @Data
    static class TestConfig {
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

}
