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
import io.github.selcukes.databind.excel.ExcelMapper;
import io.github.selcukes.databind.exception.DataMapperException;
import lombok.Data;
import org.testng.annotations.Test;

import java.util.stream.Stream;

public class ExcelMapperTest {

    @Data
    @DataFile(fileName = "TestData.xlsx", sheetName = "Smoke")
    static class SampleExcel {
        @Key(name = "Screen")
        private String screen;
        @Key(name = "Feature")
        private String feature;
        @Key(name = "Test")
        private String test;
        private String run;
    }

    @Test
    public void excelMapperTest() {
        Stream<SampleExcel> pojoStream = ExcelMapper.parse(SampleExcel.class);
        pojoStream.forEach(System.out::println);
    }

    @Interpolate(substitutor = EnvPropSubstitutor.class)
    @Data
    @DataFile(fileName = "TestData.xlsx", sheetName = "Yahoo")
    static class SampleExcel1 {
        @Key(name = "First Name")
        String firstName;
        @Key(name = "Last Name")
        String lastName;
        String date;
    }

    @Test
    public void interpolateExcelMapperTest() {
        Stream<SampleExcel1> excelStream = ExcelMapper.parse(SampleExcel1.class);
        excelStream.forEach(System.out::println);
    }

    @Test(expectedExceptions = DataMapperException.class)
    public void excelMapperNegativeTest() {
        ExcelMapper.parse(Selcukes.class);
    }

    @Data
    @DataFile(sheetName = "Smoke")
    static class Selcukes {

    }

}
