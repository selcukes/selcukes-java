/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.tests;

import io.github.selcukes.core.commons.Await;
import io.github.selcukes.core.config.ConfigFactory;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

public class AwaitTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @BeforeTest
    public void beforeTest() {
        ConfigFactory.loadLoggerProperties();
    }
    @Test
    public void awaitTest() throws Exception {
        File file=new File("hello.jpg");
        String s1 = "Hello";
        String s2 = "hello";
        Await.await().poll(2)
            .atMax(10)
            .until(file::exists);

        Await.await().poll(2)
            .atMax(10)
            .until(() -> s1.equalsIgnoreCase(s2));

    }
}
