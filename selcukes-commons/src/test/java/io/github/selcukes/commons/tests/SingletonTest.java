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

import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;

public class SingletonTest {

    @Test
    public void testInstanceOfReturnsSameInstance() {
        DummyClass obj1 = Singleton.instanceOf(DummyClass.class);
        DummyClass obj2 = Singleton.instanceOf(DummyClass.class);

        assertSame(obj1, obj2);
    }

    @Test
    public void testDifferentParametersReturnsDifferentInstances() {
        DummyClass obj1 = Singleton.instanceOf(DummyClass.class, "Hey");
        DummyClass obj2 = Singleton.instanceOf(DummyClass.class, "Hello");
        assertNotSame(obj1.getName(), obj2.getName());
    }

    @Test
    public void testDifferentClassesReturnsDifferentInstances() {
        DummyClass obj1 = Singleton.instanceOf(DummyClass.class);
        OtherClass obj2 = Singleton.instanceOf(OtherClass.class);
        assertNotSame(obj1, obj2);
    }

    private static class DummyClass {
        private final String name;

        DummyClass() {
            this.name = "MyClass";
        }

        DummyClass(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    private static class OtherClass {
        // Singleton object for testing
    }
}
