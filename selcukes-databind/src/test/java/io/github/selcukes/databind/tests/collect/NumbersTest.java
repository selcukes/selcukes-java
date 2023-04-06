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

package io.github.selcukes.databind.tests.collect;

import io.github.selcukes.databind.collections.Numbers;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;

public class NumbersTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testToBigDecimal() {
        Numbers numbers = Numbers.getInstance();

        // Test converting valid string input to BigDecimal
        String validNumber = "123,456.78";
        BigDecimal expected = new BigDecimal("123456.78");
        BigDecimal actual = numbers.parseBigDecimal(validNumber);
        assertEquals(actual, expected);

        // Test converting empty string input to BigDecimal
        String emptyNumber = "";
        expected = BigDecimal.ZERO;
        actual = numbers.parseBigDecimal(emptyNumber);
        assertEquals(actual, expected);

        // Test converting invalid string input to BigDecimal
        String invalidNumber = "abc";
        numbers.parseBigDecimal(invalidNumber);

    }

    @Test
    public void testFormat() {
        Numbers numbers = Numbers.getInstance();
        // Test formatting positive BigDecimal
        BigDecimal positiveNumber = new BigDecimal("123456.78");
        String expected = "123,456.78";
        String actual = numbers.format(positiveNumber);
        assertEquals(actual, expected);

        // Test formatting negative BigDecimal
        BigDecimal negativeNumber = new BigDecimal("-123456.78");
        expected = "-123,456.78";
        actual = numbers.format(negativeNumber);
        assertEquals(actual, expected);
    }
}
