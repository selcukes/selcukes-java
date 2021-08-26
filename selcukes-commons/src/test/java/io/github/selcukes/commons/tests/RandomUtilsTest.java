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

import io.github.selcukes.commons.helper.RandomUtils;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import org.testng.annotations.Test;

public class RandomUtilsTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void randomTest() {
        logger.info(() -> RandomUtils.randomAscii(10));
        logger.info(() -> RandomUtils.randomNumeric(10));
        logger.info(() -> RandomUtils.randomAlphabetic(10));
        logger.info(() -> RandomUtils.randomAlphaNumeric(10));
    }
}
