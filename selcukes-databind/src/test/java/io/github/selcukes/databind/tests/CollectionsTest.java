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
        System.out.println(Streams.mapOfList(listMap));
    }

    @Test
    public void listOfMapTest() {
        var listMap = List.of(
            List.of("a", "b", "c"),
            List.of("1", "2", "3"),
            List.of("4", "5", "6"));
        System.out.println(Streams.listOfMap(listMap));
    }
}
