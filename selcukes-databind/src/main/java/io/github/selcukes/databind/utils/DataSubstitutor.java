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

import io.github.selcukes.collections.Clocks;
import io.github.selcukes.collections.StringHelper;
import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.properties.PropertiesMapper;
import lombok.experimental.UtilityClass;

import static java.lang.String.format;

@UtilityClass
public class DataSubstitutor {

    /**
     * Substitutes any placeholders in the given string with the values of the
     * corresponding system properties.
     * <p>
     * Example usage:
     *
     * <pre>{@code
     * String name = "${user.name}";
     * String resolvedName = DataSubstitutor.substituteSystemProperty(name);
     * }</pre>
     * <p>
     * If the system property "user.name" is set to "Ramesh", the resulting
     * value of `resolvedName` will be "Ramesh".
     *
     * @param  name                the input string containing placeholders for
     *                             system properties
     * @return                     the input string with placeholders replaced
     *                             by system property values
     * @throws DataMapperException if the substitution fails, for example
     *                             because a required system property is not
     *                             defined
     */
    public String substituteSystemProperty(String name) {
        try {
            return StringHelper.interpolate(name, matcher -> PropertiesMapper.systemProperties().getProperty(matcher));
        } catch (Exception e) {
            throw new DataMapperException(format("Failed to substitute system property %s. " +
                    "Please ensure that the property is defined.",
                name), e);
        }
    }

    /**
     * If the value is "date" or "datetime", then return the current date or
     * date/time in the specified format. Otherwise, return the value of the
     * system property with the specified name
     *
     * @param  value  The value to be substituted.
     * @param  format The format of the date/time.
     * @return        The value of the system property with the given name.
     */
    public String substitute(final String value, final String format) {
        if (value.equalsIgnoreCase("date")) {
            return Clocks.date(format);
        } else if (value.equalsIgnoreCase("datetime")) {
            return Clocks.dateTime(format);
        } else {
            return PropertiesMapper.systemProperties().getProperty(value);
        }
    }
}
