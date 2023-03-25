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

import io.github.selcukes.databind.utils.Try;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

public class TryTest {

    @Test
    public void testOf() {
        int result = Try.of(() -> 1);
        assertEquals(result, 1);
    }

    @Test
    public void testOfWithCheckedException() {
        assertThrows(IOException.class, () -> Try.<Object, IOException> of(() -> {
            throw new IOException("Test exception");
        }, IOException::new));
    }

    @Test
    public void testOfWithRuntimeException() {
        assertThrows(RuntimeException.class, () -> Try.of(() -> {
            throw new RuntimeException("Test exception");
        }));
    }

    @Test
    public void testIgnore() {
        Try.ignore(() -> {
            throw new RuntimeException("Test exception");
        });
        // No exception should be thrown
    }

    @Test
    public void testAutoCloseable() {
        class TestResource implements AutoCloseable {
            boolean closed = false;

            @Override
            public void close() {
                closed = true;
            }
        }

        TestResource resource = new TestResource();
        Try.of(resource, (r, e) -> {
            // Do nothing
        });

        assertTrue(resource.closed);
    }

}
