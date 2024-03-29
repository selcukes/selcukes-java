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

import static java.lang.Integer.parseInt;

/**
 * "This class converts a String to an Integer."
 * <p>
 * The class extends DefaultConverter, which is a class that implements the
 * Converter interface
 */
public class IntegerConverter extends DefaultConverter<Integer> {
    @Override
    public Integer convert(final String value) {
        return parseInt(value);
    }

    @Override
    public Type getType() {
        return Integer.TYPE;
    }
}
