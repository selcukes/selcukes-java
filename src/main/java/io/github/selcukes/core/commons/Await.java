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

package io.github.selcukes.core.commons;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Await {

    private final Lock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    private long maxTimeout = 1;
    private long pollTimeout = 1;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

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

    public Await timeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    /**
     * Do await until default timeout milliseconds.
     *
     * @throws InterruptedException interrupted exception
     */
    public void until() throws InterruptedException {

        lock.lock();
        try {
            condition.await(maxTimeout, timeUnit);
        } finally {
            lock.unlock();
        }
    }

    public void until(Callable<Boolean> conditionEvaluator) throws Exception {

        lock.lock();
        try {
            while (!conditionEvaluator.call())
                condition.await();

            condition.signal();
        } finally {
            lock.unlock();
        }

    }

}

