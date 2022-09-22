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

package io.github.selcukes.commons.tests.helper;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.databind.utils.Clocks;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class DateHelperTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testDate() {
        logger.info(() -> Clocks.dateTime(Clocks.DATE_TIME_FILE_FORMAT));
        logger.info(Clocks::timeStamp);
        logger.info(() -> Clocks.dateOf("10/08/2021", "").toString());
        LocalDate currentDate = Clocks.nowDate();
        logger.info(() -> Clocks.format(currentDate, "MM-dd-YY"));
        logger.info(() -> Clocks.timeStamp(1000));

    }
}
