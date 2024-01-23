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

package io.github.selcukes.commons.tests.helper;

import io.github.selcukes.collections.Maps;
import io.github.selcukes.collections.Streams;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CollectionUtilsTest {
    private final List<String> headers = List.of("NEW",
        "RUNNABLE",
        "BLOCKED",
        "WAITING",
        "TIMED_WAITING",
        "TERMINATED");
    private final List<String> values = List.of("1", "2", "3", "4", "5", "6 ");

    @Test
    public void enumTest() {
        Assert.assertEquals(Streams.of(State.class).toList(), headers);
    }

    @Test
    public void mapTest() {
        var stringMap = Maps.of(headers, values, "");
        Assert.assertEquals(stringMap.get("WAITING"), "4");
    }

    @Test
    public void integerListMapTest() {
        var integerList = List.of(1, 2, 3, 4);
        var stringIntegerMap = Maps.of(headers, integerList, 0);
        Assert.assertEquals(stringIntegerMap.get("TERMINATED"), 0);
    }

    @Test
    public void trimTest() {
        Assert.assertEquals(Streams.trim(values).get(5), "6");
    }

    private enum State {
        NEW,
        RUNNABLE,
        BLOCKED,
        WAITING,
        TIMED_WAITING,
        TERMINATED
    }
}
