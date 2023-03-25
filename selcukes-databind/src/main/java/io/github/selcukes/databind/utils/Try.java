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

package io.github.selcukes.databind.utils;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents the result of an operation that may fail with an exception. A
 * `Try` instance can either contain a result of type `T` or an exception.
 *
 * @param <T> The type of the result that can be contained in the `Try`
 *            instance.
 */
public class Try<T> {
    private final T result;
    private final Exception exception;

    /**
     * Constructs a new `Try` instance with the given result and exception.
     *
     * @param result    The result of the operation, or `null` if an exception
     *                  was thrown.
     * @param exception The exception that was thrown, or `null` if the
     *                  operation succeeded.
     */
    private Try(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
    }

    /**
     * Runs the given `runnable` without throwing any checked exceptions. If the
     * `runnable` throws an exception, a `Try` instance with the exception is
     * returned.
     *
     * @param  runnable The `CheckedRunnable` to run.
     * @param  <T>      The type of the result that can be contained in the
     *                  `Try` instance.
     * @return          A `Try` instance with the result set to `null` and the
     *                  exception set to the one thrown by the `runnable`.
     */
    public static <T> Try<T> ignore(CheckedRunnable runnable) {
        try {
            runnable.run();
            return new Try<>(null, null);
        } catch (Exception e) {
            return new Try<>(null, e);
        }
    }

    /**
     * Invokes the given `supplier` and returns a `Try` instance with the result
     * or the exception thrown by the `supplier`. The `exceptionMapper` is used
     * to map the caught exception to a runtime exception to the desired type.
     *
     * @param  supplier         The `CheckedSupplier` to invoke.
     * @param  exceptionMapper  The `Function` that maps the caught exception to
     *                          a runtime exception.
     * @param  <T>              The type of the result that can be contained in
     *                          the `Try` instance.
     * @return                  A `Try` instance with the result set to the
     *                          value returned by the `supplier` and the
     *                          exception set to the one caught.
     * @throws RuntimeException The exception returned by the `exceptionMapper`.
     */
    public static <T> Try<T> of(
            CheckedSupplier<T> supplier, Function<Exception, ? extends RuntimeException> exceptionMapper
    ) {
        try {
            return new Try<>(supplier.get(), null);
        } catch (Exception e) {
            throw exceptionMapper.apply(e);
        }
    }

    /**
     * Invokes the given `supplier` to create a resource of type `T` and applies
     * the `resourceMapper` to the resource. The resource is automatically
     * closed after the `resourceMapper` is applied.
     *
     * @param  supplier       The `CheckedSupplier` to create the resource.
     * @param  resourceMapper The `CheckedFunction` to apply to the resource.
     * @param  <T>            The type of the resource to create.
     * @param  <R>            The type of the result that can be contained in
     *                        the `Try` instance.
     * @return                A `Try` instance with the result set to the value
     *                        returned by the `resourceMapper` and the exception
     *                        set to the one caught.
     */
    public static <T extends AutoCloseable, R> Try<R> with(
            CheckedSupplier<T> supplier, CheckedFunction<T, R> resourceMapper
    ) {
        try (T resource = supplier.get()) {
            return new Try<>(resourceMapper.apply(resource), null);
        } catch (Exception e) {
            return new Try<>(null, e);
        }
    }

    /**
     * Returns `true` if the operation succeeded (i.e., no exception was
     * thrown).
     *
     * @return `true` if the operation succeeded, `false` otherwise.
     */
    public boolean isSuccess() {
        return exception == null;
    }

    /**
     * Returns `true` if the operation failed (i.e., an exception was thrown).
     *
     * @return `true` if the operation failed, `false` otherwise.
     */
    public boolean isFailure() {
        return exception != null;
    }

    /**
     * Returns the result held by this Try object wrapped in an Optional.
     *
     * @return an Optional containing the result held by this Try object, or an
     *         empty Optional if this Try object represents a failure
     */
    public Optional<T> get() {
        return Optional.ofNullable(result);
    }

    /**
     * Returns the cause of the failure represented by this Try object.
     *
     * @return the cause of the failure represented by this Try object, or null
     *         if this Try object represents a success
     */
    public Exception getCause() {
        return exception;
    }

    /**
     * A functional interface representing a function that takes an argument of
     * type T and returns a result of type R, and may throw an Exception.
     *
     * @param <T> the type of the input to the function
     * @param <R> the type of the result of the function
     */
    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }

    /**
     * A functional interface representing a supplier of values of type T, which
     * may throw an Exception.
     *
     * @param <T> the type of results supplied by this supplier
     */
    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    /**
     * A functional interface representing an operation that may throw an
     * Exception.
     */
    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws Exception;
    }
}
