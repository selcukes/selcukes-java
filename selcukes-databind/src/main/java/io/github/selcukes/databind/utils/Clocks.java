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

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.ofNullable;

@UtilityClass
/**
 * A class that represents a clock.
 */
public class Clocks {
    // A constant that is used to format the date.
    public static final String DATE_FORMAT = "MM/dd/yyyy";
    // A constant that is used to format the date.
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    // A constant that is used to format the date.
    public static final String DATE_TIME_FILE_FORMAT = "ddMMMyyyy-hh-mm-ss";
    // A constant that is used to format the date.
    public static final String TIMESTAMP_FORMAT = "MM/dd/yyyy hh:mm:ss";

    /**
     * Return the current date.
     *
     * @return The current date.
     */
    public LocalDate nowDate() {
        return LocalDate.now();
    }

    /**
     * Return the current date and time.
     *
     * @return LocalDateTime.now()
     */
    public LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    /**
     * > Returns the current date in the specified format
     *
     * @param  format The format of the date.
     * @return        A string representation of the current date in the format
     *                specified.
     */
    public String date(final String format) {
        return format(nowDate(), format);
    }

    /**
     * It takes a date and a format and returns a LocalDate
     *
     * @param  date   The date to be parsed.
     * @param  format The format of the date string.
     * @return        A LocalDate object
     */
    public LocalDate dateOf(final String date, final String format) {
        var dateTimeFormatter = dateTimeFormatter(format, DATE_FORMAT);
        try {
            return LocalDate
                    .parse(date, dateTimeFormatter);
        } catch (DateTimeException e) {
            String newFormat = format.isBlank() ? DATE_FORMAT : format;
            throw new DateTimeException(date + " could not be parsed with '" + newFormat + "' Format");
        }
    }

    /**
     * > Returns the current date and time in the specified format
     *
     * @param  format The format of the date.
     * @return        A string representation of the current date and time.
     */
    public String dateTime(final String format) {
        return format(nowDateTime(), format);
    }

    /**
     * > It takes a date time string and a format string and returns a
     * LocalDateTime object
     *
     * @param  dateTime The date time string to be parsed.
     * @param  format   The format of the dateTime string.
     * @return          A LocalDateTime object
     */
    public LocalDateTime dateTimeOf(final String dateTime, final String format) {
        var dateTimeFormatter = dateTimeFormatter(format, DATE_TIME_FORMAT);
        try {
            return LocalDateTime
                    .parse(dateTime, dateTimeFormatter);
        } catch (DateTimeException e) {
            String newFormat = format.isBlank() ? DATE_TIME_FORMAT : format;
            throw new DateTimeException(dateTime + " could not be parsed with '" + newFormat + "' Format");
        }
    }

    /**
     * > Returns a string representation of the current date and time in the
     * format "yyyy-MM-dd HH:mm:ss.SSS"
     *
     * @return A string of the current date and time in the format of
     *         "yyyy-MM-dd HH:mm:ss"
     */
    public String timeStamp() {
        return dateTime(TIMESTAMP_FORMAT);
    }

    /**
     * > It takes a long value representing the number of milliseconds since the
     * epoch, converts it to a LocalDateTime, and then formats it using the
     * TIMESTAMP_FORMAT constant
     *
     * @param  epochMilli The epoch time in milliseconds.
     * @return            A string representation of the date and time.
     */
    public String timeStamp(long epochMilli) {
        return format(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault()),
            TIMESTAMP_FORMAT);
    }

    /**
     * It takes a date and a format string, and returns a string representation
     * of the date in the specified format
     *
     * @param  date   The date to be formatted
     * @param  format The format to use for the date.
     * @return        A string representation of the date in the format
     *                specified.
     */
    public String format(final LocalDate date, final String format) {
        return date.format(dateTimeFormatter(format, DATE_FORMAT));
    }

    /**
     * It takes a date time and a format string, and returns a string
     * representation of the date time in the specified format
     *
     * @param  dateTime The date time to format
     * @param  format   The format to use for the date.
     * @return          A string representation of the dateTime object.
     */
    public String format(final LocalDateTime dateTime, final String format) {
        return dateTime.format(dateTimeFormatter(format, DATE_TIME_FORMAT));
    }

    /**
     * If the format is not null and not empty, return the format, otherwise
     * return the default format.
     *
     * @param  format        The format to use.
     * @param  defaultFormat The default format to use if the format parameter
     *                       is null or empty.
     * @return               A DateTimeFormatter
     */
    public static DateTimeFormatter dateTimeFormatter(final String format, final String defaultFormat) {
        return ofPattern(ofNullable(format)
                .filter(f -> !f.isEmpty())
                .orElse(defaultFormat));
    }

    /**
     * Return the last day of the month for the given date.
     *
     * @param  date The date to adjust
     * @return      The last day of the month.
     */
    public LocalDate lastDayOfMonth(final LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

}
