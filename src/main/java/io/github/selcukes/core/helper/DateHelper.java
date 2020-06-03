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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    public final String DEFAULT_DATE_PATTERN = "ddMMMyyyy-hh-mm-ss";
    public final String TIMESTAMP_FORMAT = "dd/MM/yyyy hh:mm:ss.SSS";

    private DateTimeFormatter dtf;

    private final LocalDate localDate;

    private final LocalDateTime localDateTime;


    public DateHelper() {
        this.localDateTime = LocalDateTime.now();
        this.localDate = LocalDate.now();
        this.dtf = setDateFormat(DEFAULT_DATE_PATTERN);
    }

    public static DateHelper get() {
        return new DateHelper();
    }

    public DateTimeFormatter setDateFormat(String dateFormat) {
        return DateTimeFormatter.ofPattern(dateFormat);
    }

    public String dateTime() {
        return localDateTime.format(dtf);
    }

    public String date() {
        return localDate.format(dtf);
    }

    public String timeStamp() {
        return localDateTime.format(setDateFormat(TIMESTAMP_FORMAT));
    }

    public String timeStamp(long date) {

        return timeStamp(date, TIMESTAMP_FORMAT);
    }

    public String timeStamp(long date, String dateFormat) {

        return setTimeStampDateFormat(dateFormat).format(Instant.ofEpochMilli(date));
    }

    public DateTimeFormatter setTimeStampDateFormat(String dateFormat) {
        ZoneId zone = ZoneId.systemDefault();
        return setDateFormat(dateFormat).withZone(zone);
    }

    public String futureDate(int noOfDays) {
        return localDate.plusDays(noOfDays).toString();
    }

    public String pastDate(int noOfDays) {
        return localDate.minusDays(noOfDays).toString();
    }

}
