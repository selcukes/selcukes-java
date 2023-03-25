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
import io.github.selcukes.databind.utils.Try;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Await {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final long maxTimeoutInMillis;
    private final long pollTimeoutInMillis;

    private static final long DEFAULT_POLL_TIMEOUT_MILLIS = 100;

    public Await() {
        this.maxTimeoutInMillis = 1000;
        this.pollTimeoutInMillis = DEFAULT_POLL_TIMEOUT_MILLIS;
    }

    public Await(long maxTimeoutInMillis, long pollTimeoutInMillis) {
        this.maxTimeoutInMillis = maxTimeoutInMillis;
        this.pollTimeoutInMillis = pollTimeoutInMillis;
    }

    public static Await await() {
        return new Await();
    }

    public static void until(int timeoutInSeconds) {
        until(TimeUnit.SECONDS, timeoutInSeconds);
    }

    public static void until(TimeUnit timeUnit, int timeout) {
        Try.of(() -> timeUnit.sleep(timeout),
            e -> new SelcukesException("Timeout exception", e));
    }

    public Await poll(long pollTimeoutInMillis) {
        return new Await(this.maxTimeoutInMillis, pollTimeoutInMillis);
    }

    public Await atMax(long maxTimeoutInMillis) {
        return new Await(maxTimeoutInMillis, this.pollTimeoutInMillis);
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
        while (stopwatch <= maxTimeoutInMillis) {
            try {
                if (conditionEvaluator.call().equals(Boolean.TRUE)) {
                    logger.info(() -> "Condition met within the given time");
                    return true;
                }
                logger.debug(() -> "Waiting for condition to be met...");
                TimeUnit.MILLISECONDS.sleep(pollTimeoutInMillis);
                stopwatch += pollTimeoutInMillis;
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
