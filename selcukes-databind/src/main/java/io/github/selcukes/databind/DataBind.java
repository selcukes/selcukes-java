/*
 *
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
 *
 */

package io.github.selcukes.databind;

import java.nio.file.Path;

/**
 * The interface Data bind.
 */
interface DataBind {
    /**
     * Parse t.
     *
     * @param <T>       the type parameter
     * @param path      the path
     * @param dataClass the data class
     * @return the t
     */
    <T> T parse(final Path path, final Class<T> dataClass);

    /**
     * Write.
     *
     * @param <T>   the type parameter
     * @param path  the path
     * @param value the value
     */
    <T> void write(final Path path, final T value);
}
