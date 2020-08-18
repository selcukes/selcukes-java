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
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;

public class AwaitTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void awaitTest() throws Exception {
        File file = new File("hello.jpg");
        String s1 = "Hello";
        String s2 = "hello";
        long startTime = System.currentTimeMillis() / 1000;
        Await.await().poll(2)
            .atMax(10)
            .until(file::exists);
        long endTime = System.currentTimeMillis() / 1000;
        long duration = endTime - startTime;
        logger.info(() -> "Duration:" + duration);
        Await.await().poll(2)
            .atMax(10)
            .until(() -> s1.equalsIgnoreCase(s2));

    }
}
