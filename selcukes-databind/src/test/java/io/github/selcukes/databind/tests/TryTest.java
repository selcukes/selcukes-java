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

import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.Try;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class TryTest {

    @Test(expectedExceptions = RuntimeException.class)
    public void testOfWithCheckedException() {
        Try.of(() -> {
            throw new IOException("Test exception");
        });
    }

    @Test(expectedExceptions = DataMapperException.class)
    public void testCheckedExceptionAs() {
        Try.of(() -> {
            throw new InterruptedException("Test exception");
        }, DataMapperException::new);
    }

    @Test
    public void testOfCheckedRunnableAndCheckedSupplier() {
        var result = Try.of(() -> "Hello");
        assertEquals(result.get().orElseThrow(), "Hello");

        Try<String> result1 = Try.of(() -> {
            // Do Nothing
        });

        assertEquals(result1.get().orElse("Its Me"), "Its Me");
    }

    @Test
    public void testIgnore() {
        Try.attempt(() -> {
            throw new RuntimeException("Test exception");
        });
        var result = Try.attempt(() -> {
            throw new IOException("oops");
        });
        assertTrue(result.isFailure());
        assertNotNull(result.getCause());
    }

    @Test
    public void testAutoCloseable() {
        class TestResource implements AutoCloseable {
            boolean closed = false;

            public int doSomething() {
                return 42;
            }

            @Override
            public void close() {
                closed = true;
            }
        }
        TestResource resource = new TestResource();
        var result = Try.with(() -> resource, TestResource::doSomething);
        assertTrue(resource.closed);
        assertEquals(result.get().orElseThrow(), 42);
    }

    @Test
    void testFlatMap() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> value + " result"));
        assertEquals(result.get().orElseThrow(), "Hello result");
    }

    @Test
    void testRecover() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> {
                    throw new IOException("Something wrong");
                }))
                .recover(() -> "fallback");
        assertEquals(result, "fallback");
    }

    @Test
    void testFold() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> value + " result"))
                .fold(ex -> "error", value -> value);

        assertEquals(result, "Hello result");
    }

    @Test
    void testFold1() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> {
                    throw new IOException("Something wrong");
                }))
                .fold(ex -> "error", value -> value);
        assertEquals(result, "error");
    }

}
