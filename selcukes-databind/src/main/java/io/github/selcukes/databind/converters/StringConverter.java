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

/**
 * "This class converts a String to a String."
 * <p>
 * The `DefaultConverter` class is a generic class that takes a single type parameter. In this case, the type parameter is
 * `String`. The `DefaultConverter` class is an abstract class that requires you to implement the `convert` method. The
 * `convert` method takes a single parameter of type `String` and returns a `String`
 */
public class StringConverter extends DefaultConverter<String> {
    @Override
    public String convert(final String value) {
        return value;
    }
}
