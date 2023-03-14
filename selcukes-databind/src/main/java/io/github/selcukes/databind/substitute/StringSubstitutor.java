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

import io.github.selcukes.databind.utils.StringHelper;

import java.util.Properties;

/**
 * It replaces all occurrences of ${key} with the value of the key in the
 * properties object
 */
public class StringSubstitutor extends DefaultSubstitutor {

    @Override
    public String replace(final Properties variables, final String key, final String format) {
        String value = variables.getProperty(key);
        return StringHelper.interpolate(value, matcher -> StringHelper.substitute(matcher, format));
    }

    @Override
    public String replace(final String strToReplace, final String format) {
        return StringHelper.interpolate(strToReplace, matcher -> StringHelper.substitute(matcher, format));
    }

}
