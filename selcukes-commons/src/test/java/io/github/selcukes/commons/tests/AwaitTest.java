/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.Await;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AwaitTest {

    @Test
    public void testUntil() {
        long startTime = System.currentTimeMillis();
        Await.until(1);
        long elapsedTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue(elapsedTime >= 1000 && elapsedTime < 1100);
    }

    @Test
    public void testUntilConditionMet() {
        Await await = Await.await().poll(50).atMax(200);
        boolean result = await.until(() -> true);
        Assert.assertTrue(result);
    }

    @Test
    public void testUntilConditionNotMet() {
        Await await = Await.await().poll(50).atMax(200);
        boolean result = await.until(() -> false);
        Assert.assertFalse(result);
    }

    @Test
    public void testUntilConditionThrowsException() {
        Await await = Await.await().poll(50).atMax(200);
        boolean result = await.until(() -> {
            throw new RuntimeException("Test exception");
        });
        Assert.assertFalse(result);
    }
}
