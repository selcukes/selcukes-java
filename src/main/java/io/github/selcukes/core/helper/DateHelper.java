/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class DateHelper {
    private static final String DEFAULT_DATE_PATTERN = "ddMMMyyyy-hh-mm-ss";
    @Builder.Default
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    @Builder.Default
    private LocalDate localDate = LocalDate.now();
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();

    public String getDateTime() {
        return localDateTime.format(dtf);
    }

    public String getFutureDate(int noOfDays) {
        return localDate.plusDays(noOfDays).toString();
    }

    public String getPastDate(int noOfDays) {
        return localDate.minusDays(noOfDays).toString();
    }
    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
    }
}
