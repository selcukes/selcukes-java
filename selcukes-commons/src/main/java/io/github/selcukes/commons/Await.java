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

package io.github.selcukes.commons;

import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Await {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private long maxTimeout = 1;
    private long pollTimeout = 1;

    public static Await await() {

        return new Await();
    }

    public Await poll(long pollTimeout) {
        this.pollTimeout = pollTimeout;
        return this;
    }

    public Await atMax(long maxTimeout) {
        this.maxTimeout = maxTimeout;
        return this;
    }

    public void until(Callable<Boolean> conditionEvaluator) throws Exception {
        long stopwatch = 1;
        while (Boolean.FALSE.equals(conditionEvaluator.call()) && stopwatch <= maxTimeout) {
            logger.debug(() -> "Waiting...");
            TimeUnit.SECONDS.sleep(pollTimeout);
            stopwatch += pollTimeout;
        }
        if (stopwatch > maxTimeout) {
            logger.error(() -> "Condition not successful");
        } else {
            logger.info(() -> "Condition successful");
        }

    }

    public static void until(int timeoutInSeconds) {
        try {
            TimeUnit.SECONDS.sleep(timeoutInSeconds);
        } catch (Exception e) {
            throw new SelcukesException("Timeout exception : ", e);
        }
    }

    public static void until(TimeUnit timeUnit, int timeout) {
        try {
            timeUnit.sleep(timeout);
        } catch (Exception e) {
            throw new SelcukesException("Timeout exception : ", e);
        }
    }
}

