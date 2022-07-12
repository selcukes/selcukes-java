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
import lombok.Data;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;

public class PropertiesMapperTest {

    @Test
    public void testProperties() {
        var testConfig = PropertiesMapper.parse(TestConfig.class);
        System.out.println(testConfig.getUserName());
        if (testConfig.isTest())
            System.out.println(testConfig.getDate());
        System.out.println(testConfig.getOsName());
        System.out.println(testConfig.getJim());
        System.out.println(testConfig.getMass());
        System.out.println(testConfig.getHelloDate());

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
        @Key(name = "helloDate", format = "MM/dd/yyyy")
        LocalDate helloDate;
        @Key(name = "selcukes.jim")
        int jim;
        @Key(name = "mass", converter = ListStringConverter.class)
        List<String> mass;
    }

}
