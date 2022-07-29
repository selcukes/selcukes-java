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

package io.github.selcukes.databind.substitute;

import java.util.Properties;

public interface Substitutor {
    /**
     * Replace all occurrences of variables within the value associated with the given key in the given properties,
     * optionally formatting them
     *
     * @param variables The variables to use for replacement.
     * @param key       The key to be replaced.
     * @param format    The format of the string to be replaced.
     * @return A string with the value of the key in the properties file.
     */
    String replace(Properties variables, String key, final String format);

    /**
     * It replaces all occurrences of the string "strToReplace" with the string "format".
     *
     * @param strToReplace The string to be replaced.
     * @param format       The format of the string to be replaced.
     * @return A string with the format of the date.
     */
    String replace(String strToReplace, final String format);
}
