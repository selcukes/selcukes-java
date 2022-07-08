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

import io.github.selcukes.commons.helper.CollectionUtils;
import io.github.selcukes.databind.utils.Maps;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CollectionUtilsTest {
    private enum State {
        NEW,
        RUNNABLE,
        BLOCKED,
        WAITING,
        TIMED_WAITING,
        TERMINATED;
    }

    private final List<String> headers = List.of("NEW",
        "RUNNABLE",
        "BLOCKED",
        "WAITING",
        "TIMED_WAITING",
        "TERMINATED");
    private final List<String> values = List.of("1", "2", "3", "4", "5", "6 ");

    @Test
    public void enumTest() {
        Assert.assertEquals(CollectionUtils.toList(State.class), headers);
    }

    @Test
    public void mapTest() {
        Map<String, String> stringMap = Maps.of(headers, values);
        Assert.assertEquals(stringMap.get("WAITING"), "4");
    }

    @Test
    public void trimTest() {
        Assert.assertEquals(CollectionUtils.trim(values).get(5), "6");
    }
}
