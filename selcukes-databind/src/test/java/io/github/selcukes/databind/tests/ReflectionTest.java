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

import io.github.selcukes.collections.Reflections;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ReflectionTest {
    @Data
    @AllArgsConstructor
    private static class MyClass {
        private String value;

        private static String printName(String name) {
            return name;
        }
    }

    @Test
    public void testNewInstance() {
        var myObject = Reflections.newInstance(MyClass.class, "test");
        assertEquals(myObject.getValue(), "test");
    }

    @Test
    public void testInvokeMethod() {
        var myObject = new MyClass("initial value");
        Reflections.invokeMethod(myObject, "setValue", "new value");
        assertEquals(myObject.getValue(), "new value");
    }

    @Test
    public void testInvokeStaticMethod() {
        var name = Reflections.invokeStaticMethod(MyClass.class, "printName", "Hello");
        assertEquals(name, "Hello");
    }

    @Test
    public void testSetField() {
        var myObject = new MyClass("initial value");
        Reflections.setFieldValue(myObject, "value", "test");
        assertEquals(myObject.getValue(), "test");
    }
}
