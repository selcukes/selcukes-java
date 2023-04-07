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

import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * Utility class for parsing and formatting decimal numbers using a specified
 * pattern. This class uses a {@link DecimalFormat} object to perform the
 * parsing and formatting. The default pattern used by the class is "#,##0.00",
 * which formats numbers with thousands separators and two decimal places.
 */
public final class DecimalNumber {

    /**
     * The default decimal format used by this class.
     */
    private final DecimalFormat decimalFormat;

    /**
     * Constructs a new DecimalNumber object with the specified pattern.
     *
     * @param pattern the pattern used to format and parse decimal numbers
     */
    public DecimalNumber(String pattern) {
        this.decimalFormat = new DecimalFormat(pattern);
        this.decimalFormat.setParseBigDecimal(true);
    }

    /**
     * Constructs a new DecimalNumber object with the default pattern
     * "#,##0.00".
     */
    public DecimalNumber() {
        this("#,##0.00");
    }

    /**
     * Parses the specified string into a {@link BigDecimal} using the default
     * format.
     *
     * @param  number                   the string to be parsed
     * @return                          the {@link BigDecimal} represented by
     *                                  the string argument
     * @throws IllegalArgumentException if the string is not in a valid format
     */
    public BigDecimal parseBigDecimal(@NonNull String number) {
        try {
            return number.isBlank() ? BigDecimal.ZERO : (BigDecimal) this.decimalFormat.parse(number);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid input: " + number, e);
        }
    }

    /**
     * Parses the specified string into a {@code double} using the default
     * format.
     *
     * @param  number                   the string to be parsed
     * @return                          the {@code double} represented by the
     *                                  string argument
     * @throws IllegalArgumentException if the string is not in a valid format
     */
    public double parseDouble(@NonNull String number) {
        return parseBigDecimal(number).doubleValue();
    }

    /**
     * Formats the specified number using the default format.
     *
     * @param  number the number to be formatted
     * @return        the string representation of the number in the default
     *                format
     */
    public String format(Number number) {
        return this.decimalFormat.format(number);
    }

    /**
     * Sets the rounding mode used by this instance of {@code DecimalNumber}.
     *
     * @param roundingMode the rounding mode to be used
     */
    public void setRoundingMode(RoundingMode roundingMode) {
        this.decimalFormat.setRoundingMode(roundingMode);
    }

    /**
     * Sets the locale used by this instance of {@code DecimalNumber}.
     *
     * @param locale the locale to be used
     */
    public void setLocale(Locale locale) {
        var symbol = new DecimalFormatSymbols(locale);
        this.decimalFormat.setDecimalFormatSymbols(symbol);
    }
}
