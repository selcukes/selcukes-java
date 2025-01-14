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

package io.github.selcukes.collections.tests;

import io.github.selcukes.collections.StringHelper;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class StringHelperTest {
    @Test
    public void interpolateTest() {
        var stringMap = Map.of("module", "question");
        String label = "This is sample ${module} maker";
        String updatedLabel = StringHelper.interpolate(label, stringMap::get);
        assertEquals(updatedLabel, "This is sample question maker");
    }

    @Test
    public void stringCaseTest() {
        String camelCase = "HelloWorld";
        String snakeCase = "hello_world";
        assertEquals(StringHelper.toCamelCase(snakeCase), camelCase);
        assertEquals(StringHelper.toSnakeCase(camelCase), snakeCase);
    }

    @Test
    public void normalizeTextTest() {
        String text = "Hello\nbe";
        assertEquals(StringHelper.normalizeText(text), "Hello be");
    }

    @Test
    public void listOfListTest() {
        String text = """
                Name , Email , Phone , Country, State
                Rajeev Kumar Singh ,rajeevs@example.com,+91-9999999999,India
                Sachin Tendulkar,sachin@example.com,+91-9999999998,India
                Barak Obama,barak.obama@example.com,+1-1111111111,United States
                Donald Trump,donald.trump@example.com,+1-2222222222,United States
                , ,,
                """;

        var data = StringHelper.toListOfList(text.lines(), ",");
        data.forEach(System.out::println);
    }

    @Test
    public void keywordTest() {
        String line = "This is a Sample line containing the keyword 'Test'.";
        var keywords = List.of("test");
        assertTrue(StringHelper.containsWord(line, keywords));
    }

    @Test
    public void fieldNameTest() {
        verifyFieldName(" Account-Number-123?", "accountNumber123");
        verifyFieldName("Account Number", "accountNumber");
        verifyFieldName("account123%45", "account12345");
    }

    private void verifyFieldName(String actual, String expected) {
        assertEquals(StringHelper.toFieldName(actual), expected, "Field name should be " + expected);
    }
}
