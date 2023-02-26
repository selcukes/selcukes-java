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
import org.testng.annotations.Test;

import java.util.function.Supplier;

import static org.testng.Assert.*;

public class SingletonContextTest {

    @Test(threadPoolSize = 5, invocationCount = 10, timeOut = 10000)
    public void testReturnsSameInstanceForSameThread() {
        Supplier<MySingleton> supplier = MySingleton::new;
        SingletonContext<MySingleton> context = SingletonContext.with(supplier);

        MySingleton instance1 = context.get();
        MySingleton instance2 = context.get();

        assertSame(instance1, instance2, "Instances should be the same for the same thread");
    }

    @Test(threadPoolSize = 5, invocationCount = 10, timeOut = 10000)
    public void testReturnsDifferentInstancesForDifferentThreads() {
        Supplier<MySingleton> supplier = MySingleton::new;
        SingletonContext<MySingleton> context = SingletonContext.with(supplier);

        MySingleton instance1 = context.get();

        Thread thread = new Thread(() -> {
            MySingleton instance2 = context.get();
            assertNotSame(instance1, instance2, "Instances should be different for different threads");
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Interrupted while waiting for thread to complete");
        }
    }

    @Test(threadPoolSize = 5, invocationCount = 10, timeOut = 10000)
    public void testRemovesInstanceForCurrentThread() {
        Supplier<MySingleton> supplier = MySingleton::new;
        SingletonContext<MySingleton> context = SingletonContext.with(supplier);

        MySingleton instance1 = context.get();
        context.remove();
        MySingleton instance2 = context.get();

        assertNotSame(instance1, instance2, "Instances should be different after removal");
    }

    @Test(threadPoolSize = 5, invocationCount = 10, timeOut = 10000)
    public void testRemovesAllInstancesForCurrentThread() {
        Supplier<MySingleton> supplier = MySingleton::new;
        SingletonContext<MySingleton> context = SingletonContext.with(supplier);

        MySingleton instance1 = context.get();
        MySingleton instance2 = context.get();

        context.removeAll();

        MySingleton instance3 = context.get();
        MySingleton instance4 = context.get();

        assertNotSame(instance1, instance3, "Instances should be different after removeAll");
        assertNotSame(instance2, instance4, "Instances should be different after removeAll");
    }

    private static class MySingleton {
        private final String name;

        MySingleton() {
            this.name = "MyObject";
        }

        MySingleton(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
}
