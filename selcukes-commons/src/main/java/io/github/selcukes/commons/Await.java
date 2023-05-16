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

import io.github.selcukes.collections.Try;
import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Await {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final long maxTimeout;
    private final long pollTimeout;

    public Await() {
        this.maxTimeout = 1000;
        this.pollTimeout = 100;
    }

    public Await(long maxTimeout, long pollTimeout) {
        this.maxTimeout = maxTimeout;
        this.pollTimeout = pollTimeout;
    }

    public static Await await() {
        return new Await();
    }

    public static void until(int timeoutInSeconds) {
        until(TimeUnit.SECONDS, timeoutInSeconds);
    }

    public static void until(TimeUnit timeUnit, int timeout) {
        Try.of(() -> timeUnit.sleep(timeout))
                .orElseThrow(e -> new SelcukesException("Timeout exception", e));
    }

    public Await poll(long pollTimeout) {
        return new Await(this.maxTimeout, pollTimeout);
    }

    public Await atMax(long maxTimeout) {
        return new Await(maxTimeout, this.pollTimeout);
    }

    /**
     * Waits for the specified condition to be met, polling periodically until
     * either the condition is met or the maximum timeout is reached.
     *
     * @param  conditionEvaluator a function that evaluates the condition to be
     *                            met
     * @return                    true if the condition is met within the
     *                            maximum timeout period, false otherwise
     */
    public boolean until(Callable<Boolean> conditionEvaluator) {
        long stopwatch = 0;
        while (stopwatch <= maxTimeout) {
            try {
                if (conditionEvaluator.call().equals(Boolean.TRUE)) {
                    logger.info(() -> "Condition met within the given time");
                    return true;
                }
                logger.debug(() -> "Waiting for condition to be met...");
                TimeUnit.MILLISECONDS.sleep(pollTimeout);
                stopwatch += pollTimeout;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error(e, () -> "Interrupted while waiting for condition");
                return false;
            } catch (Exception e) {
                logger.error(e, () -> "Error while evaluating condition");
                return false;
            }
        }
        logger.error(() -> "Condition not met within the given time");
        return false;
    }
}
