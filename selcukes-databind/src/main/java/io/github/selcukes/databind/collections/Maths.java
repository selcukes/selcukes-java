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

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.function.BinaryOperator;

@UtilityClass
public class Maths {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    /**
     * Returns a binary operator that performs decimal calculations using the
     * specified operator.
     *
     * @param  operator                 the binary operator to perform the
     *                                  decimal calculation.
     * @return                          the binary operator that performs the
     *                                  decimal calculation.
     * @throws IllegalArgumentException if the input is invalid.
     */
    public BinaryOperator<String> of(BinaryOperator<BigDecimal> operator) {
        DECIMAL_FORMAT.setParseBigDecimal(true);
        return (s1, s2) -> {
            try {
                var value1 = (BigDecimal) DECIMAL_FORMAT.parse(s1);
                var value2 = (BigDecimal) DECIMAL_FORMAT.parse(s2);
                var result = operator.apply(value1, value2);
                return DECIMAL_FORMAT.format(result);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid input: " + e.getMessage());
            }
        };
    }
}
