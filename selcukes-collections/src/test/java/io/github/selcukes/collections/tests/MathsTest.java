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

package io.github.selcukes.collections.tests;

import io.github.selcukes.collections.Maths;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;

public class MathsTest {

    @Test
    public void testOfWithValidInputs() {
        // Test addition operation
        BinaryOperator<String> addOp = Maths.of(BigDecimal::add);
        String expected = "10,000.00";
        String actual = addOp.apply("5,000.00", "5,000.00");
        Assert.assertEquals(actual, expected);

        // Test subtraction operation
        BinaryOperator<String> subOp = Maths.of(BigDecimal::subtract);
        expected = "-5,000.00";
        actual = subOp.apply("0.00", "5,000.00");
        Assert.assertEquals(actual, expected);

        // Test multiplication operation
        BinaryOperator<String> mulOp = Maths.of(BigDecimal::multiply);
        expected = "25,000.00";
        actual = mulOp.apply("5,000.00", "5.00");
        Assert.assertEquals(actual, expected);

        // Test division operation
        BinaryOperator<String> divOp = Maths.of(BigDecimal::divide);
        expected = "5.00";
        actual = divOp.apply("25,000.00", "5,000.00");
        Assert.assertEquals(actual, expected);

    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testOfWithInvalidInputs() {
        BinaryOperator<String> addOp = Maths.of(BigDecimal::add);
        String invalidS1 = "abc";
        String invalidS2 = "def";
        addOp.apply(invalidS1, invalidS2);
    }

}
