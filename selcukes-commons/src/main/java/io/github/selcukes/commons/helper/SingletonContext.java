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

package io.github.selcukes.commons.helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

/**
 * A thread-safe context for managing per-thread singleton instances of a type
 * T. This class uses a ThreadLocal variable to store a thread-local map of
 * singleton instances of type T, where each thread has its own instance. The
 * instances are created lazily using a Supplier<T> object passed to the
 * constructor of this class. Example usage: SingletonContext<MySingleton>
 * context = SingletonContext.with(MySingleton::getInstance); MySingleton
 * singleton = context.get(); // returns a thread-local singleton instance of
 * MySingleton context.remove(); // removes the thread-local singleton instance
 * for the current thread
 *
 * @param <T> the type of the singleton instances managed by this context.
 */
public final class SingletonContext<T> {
    private final ThreadLocal<Map<Long, T>> threadLocal = ThreadLocal.withInitial(ConcurrentHashMap::new);
    private final Supplier<T> supplier;

    /**
     * Creates a new SingletonContext object with the specified supplier of
     * singleton instances.
     *
     * @param supplier a Supplier<T> object that provides a lazy initialization
     *                 strategy for creating singleton instances of type T.
     */
    private SingletonContext(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Creates a new SingletonContext object with the specified supplier of
     * singleton instances.
     *
     * @param  supplier a Supplier<T> object that provides a lazy initialization
     *                  strategy for creating singleton instances of type T.
     * @return          a new SingletonContext object.
     */
    public static <T> SingletonContext<T> with(Supplier<T> supplier) {
        return new SingletonContext<>(supplier);
    }

    /**
     * Returns the thread-local singleton instance of type T for the current
     * thread, creating it lazily using the supplier object passed to the
     * constructor if necessary.
     *
     * @return the thread-local singleton instance of type T for the current
     *         thread.
     */
    public T get() {
        return threadLocal.get().computeIfAbsent(Thread.currentThread().getId(), id -> supplier.get());
    }

    /**
     * Removes the thread-local singleton instance of type T for the current
     * thread, if any.
     */
    public void remove() {
        ofNullable(threadLocal.get())
                .ifPresent(map -> {
                    map.remove(Thread.currentThread().getId());
                    if (map.isEmpty()) {
                        threadLocal.remove();
                    }
                });
    }

    /**
     * Removes all thread-local singleton instances of type T for the current
     * thread. This method is not recommended for lazy-initialized singleton
     * objects.
     */
    public void removeAll() {
        threadLocal.get().remove(Thread.currentThread().getId());
    }
}
