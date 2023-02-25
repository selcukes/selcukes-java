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

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.helper.Singleton;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SingletonTest {

    @Test(expectedExceptions = AssertionError.class)
    void testSingletonInstance() {

        var instance1 = Singleton.instanceOf(MyClass.class);
        assertEquals("MyClass", instance1.getName());

        var instance2 = Singleton.instanceOf(MyClass.class);
        assertEquals(instance1, instance2);

        var instance3 = Singleton.instanceOf(MyClass.class, "MyName");
        assertEquals("MyName", instance3.getName());
    }

    static class MyClass {
        private final String name;

        MyClass() {
            this.name = "MyClass";
        }

        MyClass(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
}
