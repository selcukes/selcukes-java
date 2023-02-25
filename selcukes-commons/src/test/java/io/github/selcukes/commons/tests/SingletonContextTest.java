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

import io.github.selcukes.commons.helper.SingletonContext;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.util.function.Supplier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class SingletonContextTest {
    @Test
    void getSameInstanceForSameThread() {
        Supplier<MyObject> supplier = MyObject::new;
        SingletonContext<MyObject> context = SingletonContext.with(supplier);

        MyObject instance1 = context.get();
        MyObject instance2 = context.get();

        assertEquals(instance1, instance2);
    }

    @SneakyThrows
    @Test
    void getDifferentInstancesForDifferentThreads() {
        Supplier<MyObject> supplier = MyObject::new;
        SingletonContext<MyObject> context = SingletonContext.with(supplier);

        MyObject[] instances = new MyObject[2];

        Thread thread1 = new Thread(() -> {
            instances[0] = context.get();
        });
        Thread thread2 = new Thread(() -> {
            instances[1] = context.get();
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertNotEquals(instances[0], instances[1]);
    }

    static class MyObject {
        private final String name;

        MyObject() {
            this.name = "MyObject";
        }

        MyObject(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
}
