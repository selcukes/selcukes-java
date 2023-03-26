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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class TryTest {

    @Test(expectedExceptions = RuntimeException.class)
    public void testOfWithCheckedException() {
        Try.of(() -> {
            throw new IOException("Test exception");
        }).orElseThrow();
    }

    @Test(expectedExceptions = DataMapperException.class)
    public void testCheckedExceptionAs() {
        Try.of(() -> {
            throw new InterruptedException("Test exception");
        }).orElseThrow(DataMapperException::new);
    }

    @Test
    public void testOfCheckedRunnableAndCheckedSupplier() {
        var result = Try.of(() -> "Hello")
                .orElseThrow();
        assertEquals(result, "Hello");

        var result1 = Try.of(() -> {
            // Do nothing
        });
        assertTrue(result1.getResult().isEmpty());
    }

    @Test
    public void testIgnore() {
        var result = Try.of(() -> "Hello");
        assertEquals(result.getResult().orElseThrow(), "Hello");

        var result1 = Try.of(() -> {
            // Do nothing
        });
        assertTrue(result1.getResult().isEmpty());

        Try.of(() -> {
            throw new RuntimeException("Test exception");
        });
        var result3 = Try.of(() -> {
            throw new IOException("oops");
        });
        assertFalse(result3.isSuccess());
        assertNotNull(result3.getException());
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
        var result = Try.with(() -> resource, TestResource::doSomething)
                .orElseThrow();
        assertTrue(resource.closed);
        assertEquals(result, 42);
    }

    @Test
    void testFlatMap() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> value + " result"))
                .orElseThrow();
        assertEquals(result, "Hello result");
    }

    @Test
    void testRecover() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> {
                    throw new IOException("Something wrong");
                }))
                .orElse(() -> "fallback");
        assertEquals(result, "fallback");
    }

    @Test
    void testFold() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> value + " result"))
                .or(ex -> "error", value -> value);

        assertEquals(result, "Hello result");
    }

    @Test
    void testFold1() {
        var result = Try.of(() -> "Hello")
                .flatMap(value -> Try.of(() -> {
                    throw new IOException("Something wrong");
                }))
                .or(ex -> "error", value -> value);
        assertEquals(result, "error");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testOfWithNullSupplier() {
        Try.of((Try.CheckedRunnable) null).orElseThrow();
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testOrElseThrowWithNullExceptionSupplier() {
        Try.of(() -> {
            throw new IOException("Test exception");
        }).orElseThrow(null);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testFlatMapWithNullFunction() {
        Try.of(() -> "Hello")
                .flatMap(null)
                .orElseThrow();
    }

}
