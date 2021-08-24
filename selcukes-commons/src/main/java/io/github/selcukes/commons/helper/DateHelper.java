/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    private static final String DEFAULT_DATE_PATTERN = "ddMMMyyyy-hh-mm-ss";
    private static final String TIMESTAMP_FORMAT = "dd/MM/yyyy hh:mm:ss";
    private final LocalDate localDate;
    private final LocalDateTime localDateTime;
    private DateTimeFormatter dtf;

    public DateHelper() {
        this.localDateTime = LocalDateTime.now();
        this.localDate = LocalDate.now();
    }

    public static DateHelper get() {
        return new DateHelper();
    }

    public DateHelper setDateFormat(String dateFormat) {
        dtf = DateTimeFormatter.ofPattern(dateFormat);
        return this;
    }

    public String dateTime() {
        return localDateTime.format(getDateFormatter());
    }

    public String date() {
        return localDate.format(getDateFormatter());
    }

    public String timeStamp() {
        if (dtf == null)
            setDateFormat(TIMESTAMP_FORMAT);
        return dateTime();
    }

    public String timeStamp(long date) {

        return getTimeStampFormatter().format(Instant.ofEpochMilli(date));
    }

    public DateTimeFormatter getDateFormatter() {
        if (dtf == null)
            setDateFormat(DEFAULT_DATE_PATTERN);
        return dtf;
    }

    private DateTimeFormatter getTimeStampFormatter() {
        if (dtf == null)
            setDateFormat(TIMESTAMP_FORMAT);
        return dtf.withZone(getZoneId());
    }

    private ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

    public String futureDate(int noOfDays) {
        return localDate.plusDays(noOfDays).toString();
    }

    public String pastDate(int noOfDays) {
        return localDate.minusDays(noOfDays).toString();
    }

}
