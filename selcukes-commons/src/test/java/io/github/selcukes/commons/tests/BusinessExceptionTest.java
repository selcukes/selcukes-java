/*
 *
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
 *
 */

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.commons.helper.ExceptionHelper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BusinessExceptionTest {
    public void createException() {
        try {
            throw new IOException("Something Wrong");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createTest() {
        try {
            createException();
        } catch (Exception e) {
            throw new SelcukesException("Element not Found", e);
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testException() {
        try {
            createTest();
        } catch (Exception e) {
            ExceptionHelper.logError(e);
            ExceptionHelper.rethrow(e);
        }

    }

}
