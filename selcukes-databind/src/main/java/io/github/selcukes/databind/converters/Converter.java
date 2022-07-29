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

package io.github.selcukes.databind.converters;

import java.lang.reflect.Type;

// A generic interface.
public interface Converter<T> {
    /**
     * Convert a String to a T.
     *
     * @param value The value to be converted
     * @return The return type is the same as the type of the parameter.
     */
    T convert(String value);

    /**
     * Returns the type of the object.
     *
     * @return The type of the object.
     */
    Type getType();

    /**
     * If the format is null, then call the other convert function, otherwise, call the other convert function.
     *
     * @param value The value to convert.
     * @param format The format of the value.
     * @return The default implementation of the convert method is being returned.
     */
    default T convert(final String value, final String format) {
        return convert(value);
    }
}
