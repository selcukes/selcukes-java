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

import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * It converts a string to a double
 */
public class DoubleConverter extends DefaultConverter<Double> {
    @SneakyThrows
    @Override
    public Double convert(final String value) {
        return NumberFormat.getInstance(Locale.getDefault()).parse(value).doubleValue();
    }

    @Override
    public Type getType() {
        return Double.TYPE;
    }
}
