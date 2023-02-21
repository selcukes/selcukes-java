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

import io.github.selcukes.databind.utils.Streams;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CollectionsTest {
    @Test
    public void mapOfListTest() {
        var listMap = List.of(
            Map.of("a", 1, "b", 2),
            Map.of("a", 4, "b", 5),
            Map.of("a", 6, "b", 7));
        var mapOfList = Streams.toMapOfList(listMap);
        Assert.assertEquals(mapOfList.get("a").get(0), 1);
    }

    @Test
    public void listOfMapTest() {
        var listOfList = List.of(
            List.of("a", "b", "c"),
            List.of("1", "2", "3"),
            List.of("4", "5", "6"));
        var listMap = Streams.toListOfMap(listOfList);

        Assert.assertEquals(listMap.get(1).get("a"), "4");
    }

    @Test
    public void groupByTest() {
        var listMap = List.of(
            Map.of("Scenario", "Test1", "Name", "Ram"),
            Map.of("Scenario", "Test1", "Name", "Hello"),
            Map.of("Scenario", "Test2", "Name", "RB"),
            Map.of("Scenario", "Test2", "Name", "Pojo"),
            Map.of("Scenario", "Test3", "Name", "Babu"));
        var mapOfList = Streams.groupBy(listMap, "Scenario");
        Assert.assertEquals(mapOfList.get("Test2").get(1).get("Name"), "Pojo");
    }
}
