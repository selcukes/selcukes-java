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

import io.github.selcukes.collections.DecimalNumber;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import static org.testng.Assert.assertEquals;

public class DecimalNumberTest {
    private DecimalNumber decimalNumber;

    @BeforeMethod
    public void setUp() {
        decimalNumber = new DecimalNumber("#,##0.00");
    }

    @Test
    public void testParseBigDecimal() {
        BigDecimal actual = decimalNumber.parseBigDecimal("100,000.50");
        BigDecimal expected = new BigDecimal("100000.50");
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseBigDecimalWithInvalidInput() {
        decimalNumber.parseBigDecimal("abc");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testParseBigDecimalWithNullInput() {
        decimalNumber.parseBigDecimal(null);
    }

    @Test
    public void testParseBigDecimalWithEmptyInput() {
        BigDecimal actual = decimalNumber.parseBigDecimal("");
        BigDecimal expected = BigDecimal.ZERO;
        assertEquals(actual, expected);
    }

    @Test
    public void testParseDouble() {
        double actual = decimalNumber.parseDouble("100,000.50");
        double expected = 100000.50;
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseDoubleWithInvalidInput() {
        decimalNumber.parseDouble("abc");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testParseDoubleWithNullInput() {
        decimalNumber.parseDouble(null);
    }

    @Test
    public void testParseDoubleWithEmptyInput() {
        double actual = decimalNumber.parseDouble("");
        double expected = 0.0;
        assertEquals(actual, expected);
    }

    @Test
    public void testFormat() {
        BigDecimal bigDecimal = new BigDecimal("100000.50");
        String actual = decimalNumber.format(bigDecimal);
        String expected = "100,000.50";
        assertEquals(actual, expected);
    }

    @Test
    public void testFormatWithDouble() {
        double number = 100000.50;
        String actual = decimalNumber.format(number);
        String expected = "100,000.50";
        assertEquals(actual, expected);
    }

    @Test
    public void testSetRoundingMode() {
        decimalNumber.setRoundingMode(RoundingMode.CEILING);
        var bigDecimal = new BigDecimal("100000.499");
        var actual = decimalNumber.format(bigDecimal);
        var expected = "100,000.50";
        assertEquals(actual, expected);
    }

    @Test
    public void testSetLocale() {
        decimalNumber.setLocale(Locale.GERMANY);
        String actual = decimalNumber.format(new BigDecimal("100000.50"));
        String expected = "100.000,50";
        assertEquals(actual, expected);
    }

}
