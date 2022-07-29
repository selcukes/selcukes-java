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

package io.github.selcukes.core.tests.unit;

import io.github.selcukes.core.validation.Validation;
import org.testng.annotations.Test;

public class ValidationTest {
    @Test(expectedExceptions = { AssertionError.class })
    public void test1() {
        Validation.failWithMessage(true, "Error Message 1");
        Validation.failWithMessage(true, "Error Message 2");
        Validation.failWithMessage(true, "Error Message 3");
        Validation.failAll();
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void test2() {
        Validation.failWithMessage(true, "Error Message 4");
        Validation.failWithMessage(true, "Error Message 5");
        Validation.failWithMessage(true, "Error Message 6");
        Validation.failAll();
    }

    @Test(expectedExceptions = { AssertionError.class })
    public void test3() {
        Validation.failWithMessage(false, "Error Message 7");
    }
}
