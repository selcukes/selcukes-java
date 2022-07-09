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

package io.github.selcukes.databind.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.ofNullable;

@UtilityClass
public class Clocks {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String date(String format) {
        return LocalDate.now().format(dateTimeFormatter(format, DATE_FORMAT));
    }

    public static String dateTime(String format) {
        return LocalDateTime.now().format(dateTimeFormatter(format, DATE_TIME_FORMAT));
    }

    public static DateTimeFormatter dateTimeFormatter(String format, String defaultFormat) {
        return ofPattern(ofNullable(format)
            .filter(f -> !f.isEmpty())
            .orElse(defaultFormat));
    }
}
