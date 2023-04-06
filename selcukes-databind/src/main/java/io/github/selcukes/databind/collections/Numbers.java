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

package io.github.selcukes.databind.collections;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public final class Numbers {

    private final DecimalFormat defaultDecimalFormat;
    private static volatile Numbers numbers;

    private Numbers() {
        defaultDecimalFormat = new DecimalFormat("#,##0.00");
        defaultDecimalFormat.setParseBigDecimal(true);
    }

    public static synchronized Numbers getInstance() {
        if (numbers == null) {
            numbers = new Numbers();
        }
        return numbers;
    }

    /**
     * Parses the specified string into a BigDecimal using the default format.
     *
     * @param  number                   the string to be parsed.
     * @return                          the BigDecimal represented by the string
     *                                  argument.
     * @throws IllegalArgumentException if the string is not in a valid format.
     */
    public BigDecimal parseBigDecimal(String number) {
        try {
            return number.isBlank() ? BigDecimal.ZERO : (BigDecimal) defaultDecimalFormat.parse(number);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid input: " + number, e);
        }
    }

    /**
     * Formats the specified BigDecimal using the default format.
     *
     * @param  bigDecimal the BigDecimal to be formatted.
     * @return            the string representation of the BigDecimal in the
     *                    default format.
     */
    public String format(BigDecimal bigDecimal) {
        return defaultDecimalFormat.format(bigDecimal);
    }
}
