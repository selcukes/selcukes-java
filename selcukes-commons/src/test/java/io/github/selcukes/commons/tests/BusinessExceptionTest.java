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

import io.github.selcukes.commons.exception.BusinessException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

public class BusinessExceptionTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void createException() {

        try {
            throw new NoSuchElementException("Test not found");
        } catch (Exception e) {
            throw new BusinessException("Element not Found", e);
        }
    }

    @Test
    public void testException() {
        try {
            createException();
        } catch (BusinessException e) {
            logger.error(e, e.logError());
        }

    }

}
