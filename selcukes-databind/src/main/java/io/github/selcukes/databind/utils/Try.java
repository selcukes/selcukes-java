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

import java.util.function.Function;

/**
 * The Try class provides static methods to handle checked exceptions in a
 * functional way.
 * <p>
 * It offers ways to execute code blocks that throw checked exceptions, catch
 * them, and convert them into unchecked exceptions.
 */
public final class Try {
    /**
     * Private constructor to prevent instantiation of the class.
     */
    private Try() {
        // Private constructor
    }

    /**
     * Executes a checked Runnable and ignores any exceptions thrown by it.
     *
     * @param action the Runnable to execute
     */
    public static void ignore(CheckedRunnable action) {
        try {
            action.run();
        } catch (Exception ignored) {
            // ignore
        }
    }

    /**
     * Executes a checked Supplier and returns its result. If the Supplier
     * throws an exception, it is caught and rethrown as an unchecked exception.
     *
     * @param  supplier         the Supplier to execute
     * @param  <T>              the type of the result
     * @param  <E>              the type of the checked exception that can be
     *                          thrown
     * @return                  the result of the Supplier
     * @throws RuntimeException if the Supplier throws an exception
     */
    public static <T, E extends Exception> T of(CheckedSupplier<T, E> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw ExceptionUtils.wrapAndThrow(e, RuntimeException::new);
        }
    }

    /**
     * Executes a checked Runnable and converts any exceptions thrown by it to a
     * user-defined checked exception.
     *
     * @param  action            the Runnable to execute
     * @param  exceptionSupplier a Function to convert the caught Exception into
     *                           a user-defined checked exception
     * @param  <E>               the type of the user-defined checked exception
     * @throws E                 the user-defined checked exception, if the
     *                           Runnable throws an exception
     */
    public static <E extends Exception> void of(CheckedRunnable action, Function<Exception, E> exceptionSupplier)
            throws E {
        try {
            action.run();
        } catch (Exception e) {
            throw ExceptionUtils.wrapAndThrow(e, exceptionSupplier);
        }
    }

    /**
     * Executes a checked Supplier and converts any exceptions thrown by it to a
     * user-defined checked exception.
     *
     * @param  supplier          the Supplier to execute
     * @param  exceptionSupplier a Function to convert the caught Exception into
     *                           a user-defined checked exception
     * @param  <T>               the type of the result
     * @param  <E>               the type of the user-defined checked exception
     * @return                   the result of the Supplier
     * @throws E                 the user-defined checked exception, if the
     *                           Supplier throws an exception
     */
    public static <T, E extends Exception> T of(
            CheckedSupplier<T, E> supplier, Function<Exception, E> exceptionSupplier
    ) throws E {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw ExceptionUtils.wrapAndThrow(e, exceptionSupplier);
        }
    }

    /**
     * Executes a checked Consumer on an AutoCloseable resource. The resource is
     * automatically closed after the Consumer is executed. If the Consumer
     * throws an exception, it is caught and rethrown as an unchecked exception.
     *
     * @param <T>      the type of the AutoCloseable resource
     * @param <E>      the type of the checked exception that can be thrown by
     *                 the Consumer
     * @param resource the AutoCloseable resource to execute the Consumer on
     * @param action   the Consumer to execute
     */
    public static <T extends AutoCloseable, E extends Exception> void of(T resource, CheckedConsumer<T, E> action) {
        try (resource) {
            action.accept(resource, null);
        } catch (Exception e) {
            throw ExceptionUtils.wrapAndThrow(e, RuntimeException::new);
        }
    }

    /**
     * A functional interface for a Consumer that can throw a checked exception.
     *
     * @param <T> the type of the argument to the Consumer
     * @param <E> the type of the checked exception that can be thrown by the
     *            Consumer
     */
    @FunctionalInterface
    public interface CheckedConsumer<T, E extends Exception> {
        void accept(T t, E e) throws E;
    }

    /**
     * A functional interface for a Runnable that can throw a checked exception.
     */
    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws SelcukesCheckedException;

        class SelcukesCheckedException extends Exception {
            public SelcukesCheckedException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    }

    /**
     * A functional interface for a Supplier that can throw a checked exception.
     *
     * @param <T> the type of the result supplied by the Supplier
     * @param <E> the type of the checked exception that can be thrown by the
     *            Supplier
     */
    @FunctionalInterface
    public interface CheckedSupplier<T, E extends Exception> {
        T get() throws E;
    }

    /**
     * A utility class for working with exceptions.
     */
    private static class ExceptionUtils {

        /**
         * Wraps a checked exception in an unchecked exception using the
         * provided wrapper function. If the original exception is already an
         * unchecked exception, it is simply rethrown.
         *
         * @param  <T>       the type of the original exception
         * @param  <E>       the type of the wrapped exception
         * @param  throwable the original exception to wrap
         * @param  wrapper   a function that takes the original exception as
         *                   input and returns the wrapped exception
         * @return           the wrapped exception, which is always an unchecked
         *                   exception
         * @throws E         if the wrapper function throws an exception while
         *                   wrapping the original exception
         */
        public static <T extends Throwable, E extends Exception> RuntimeException wrapAndThrow(
                T throwable, Function<? super T, E> wrapper
        ) throws E {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else if (throwable instanceof Error) {
                throw (Error) throwable;
            } else {
                Throwable cause = throwable.getCause();
                if (cause == null) {
                    throw wrapper.apply(throwable);
                } else {
                    throw wrapper.apply((T) cause);
                }
            }
        }
    }

}
