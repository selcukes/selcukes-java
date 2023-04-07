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
import java.util.function.BinaryOperator;

@UtilityClass
public class Maths {

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
        return (s1, s2) -> {
            var number = new DecimalNumber();
            var value1 = number.parseBigDecimal(s1);
            var value2 = number.parseBigDecimal(s2);
            var result = operator.apply(value1, value2);
            return number.format(result);
        };
    }
}
