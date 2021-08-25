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

    private static final String DEFAULT_DATE_TIME_PATTERN = "ddMMMyyyy-hh-mm-ss";
    private static final String DEFAULT_DATE_PATTERN = "MM/dd/yyyy";
    private static final String TIMESTAMP_FORMAT = "MM/dd/yyyy hh:mm:ss";

    private DateTimeFormatter dtf;

    private LocalDate localDate;

    private final LocalDateTime localDateTime;

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

    public DateTimeFormatter getDateFormatter() {
        if (dtf == null)
            setDateFormat(DEFAULT_DATE_PATTERN);
        return dtf;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        if (dtf == null)
            setDateFormat(DEFAULT_DATE_TIME_PATTERN);
        return dtf;
    }

    public String formatDate(LocalDate date) {
        return date.format(getDateFormatter());
    }

    public String dateTime() {
        return localDateTime.format(getDateTimeFormatter());
    }

    public String date() {
        return formatDate(localDate);
    }

    public String timeStamp() {
        if (dtf == null)
            setDateFormat(TIMESTAMP_FORMAT);
        return dateTime();
    }

    public String timeStamp(long date) {

        return getTimeStampFormatter().format(Instant.ofEpochMilli(date));
    }

    private DateTimeFormatter getTimeStampFormatter() {
        if (dtf == null)
            setDateFormat(TIMESTAMP_FORMAT);
        return dtf.withZone(getZoneId());
    }

    private ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

    public LocalDate futureDate(int noOfDays) {
        return localDate.plusDays(noOfDays);
    }

    public LocalDate pastDate(int noOfDays) {
        return localDate.minusDays(noOfDays);
    }

    public LocalDate getLocalDate(String date) {
        return LocalDate.parse(date, getDateFormatter());
    }

    public LocalDate currentDate() {
        return localDate;
    }

    public DateHelper setDate(String date) {
        localDate = getLocalDate(date);
        return this;
    }

}
