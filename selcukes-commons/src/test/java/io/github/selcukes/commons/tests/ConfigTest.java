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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.testng.Assert.fail;

public class ConfigTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void configTest() {

        logger.info(() -> ConfigFactory.getConfig().getEnv());
        logger.info(() -> ConfigFactory.getConfig().getBaseUrl());
        logger.info(() -> ConfigFactory.getConfig().getProjectName());
    }

    @Test(threadPoolSize = 2, invocationCount = 5, timeOut = 10000)
    public void configTest2() {
        var instance1 = ConfigFactory.getConfig();
        instance1.getWeb().setHeadLess(false);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            var instance2 = ConfigFactory.getConfig();
            Assert.assertNotEquals(instance1.getWeb().isHeadLess(), instance2.getWeb().isHeadLess());
        });
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            fail("Interrupted while waiting for CompletableFuture to complete: " + e.getMessage());
        }
    }

    @Test(threadPoolSize = 2, invocationCount = 5, timeOut = 10000)
    public void configTest3() {
        var instance1 = ConfigFactory.getConfig();
        var instance2 = ConfigFactory.getConfig();
        Assert.assertEquals(instance1, instance2);
    }
}
