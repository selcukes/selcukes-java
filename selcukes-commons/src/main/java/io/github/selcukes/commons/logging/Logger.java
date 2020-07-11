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

package io.github.selcukes.commons.logging;

import java.util.function.Supplier;

/**
 * Logs messages to {@link java.util.logging.Logger}.
 * <ul>
 * <li>{@code error} maps to {@link java.util.logging.Level#SEVERE}</li>
 * <li>{@code warn} maps to {@link java.util.logging.Level#WARNING}</li>
 * <li>{@code info} maps to {@link java.util.logging.Level#INFO}</li>
 * <li>{@code config} maps to {@link java.util.logging.Level#CONFIG}</li>
 * <li>{@code debug} maps to {@link java.util.logging.Level#FINE}</li>
 * <li>{@code trace} maps to {@link java.util.logging.Level#FINER}</li>
 * </ul>
 */
public interface Logger {

    /**
     * Log the {@code message} at error level.
     *
     * @param message The message to log.
     */
    void error(Supplier<String> message);

    /**
     * Log the {@code message} and {@code throwable} at error level.
     *
     * @param throwable The throwable to log.
     * @param message   The message to log.
     */
    void error(Throwable throwable, Supplier<String> message);

    /**
     * Log the {@code message} at warning level.
     *
     * @param message The message to log.
     */
    void warn(Supplier<String> message);

    /**
     * Log the {@code message} and {@code throwable} at warning level.
     *
     * @param throwable The throwable to log.
     * @param message   The message to log.
     */
    void warn(Throwable throwable, Supplier<String> message);

    /**
     * Log the {@code message} at info level.
     *
     * @param message The message to log.
     */
    void info(Supplier<String> message);

    /**
     * Log the {@code message} and {@code throwable} at info level.
     *
     * @param throwable The throwable to log.
     * @param message   The message to log.
     */
    void info(Throwable throwable, Supplier<String> message);

    /**
     * Log the {@code message} at config level.
     *
     * @param message The message to log.
     */
    void config(Supplier<String> message);

    /**
     * Log the {@code message} and {@code throwable} at config level.
     *
     * @param throwable The throwable to log.
     * @param message   The message to log.
     */
    void config(Throwable throwable, Supplier<String> message);

    /**
     * Log the {@code message} at debug level.
     *
     * @param message The message to log.
     */
    void debug(Supplier<String> message);

    /**
     * Log {@code message} and {@code throwable} at debug level.
     *
     * @param throwable The throwable to log.
     * @param message   The message to log.
     */
    void debug(Throwable throwable, Supplier<String> message);

    /**
     * Log the {@code message} at trace level.
     *
     * @param message The message to log.
     */
    void trace(Supplier<String> message);

    /**
     * Log the {@code message} and {@code throwable} at trace level.
     *
     * @param throwable The throwable to log.
     * @param message   The message to log.
     */
    void trace(Throwable throwable, Supplier<String> message);
}