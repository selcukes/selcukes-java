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

package io.github.selcukes.databind.tests;

import io.github.selcukes.databind.utils.Clocks;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ClocksTest {
    private final String timezoneId = "America/New_York";
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private final String dateTimeZoneFormat = "dd/MM/yyyy HH:mm:ss zzz";
    private final String dateFormat = "dd-MM-yyyy";

    @Test
    public void testNowDateTime() {
        var nowDate = Clocks.dateNow();
        assertNotNull(nowDate);
        var nowDateTime = Clocks.dateTimeNow();
        assertNotNull(nowDateTime);
        var nowDateTimeZone = Clocks.dateTimeNow(timezoneId);
        assertNotNull(nowDateTimeZone);
    }

    @Test
    public void testNowDateTimeWithTimezone() {

        var nowDateTime = Clocks.dateTimeNow(timezoneId);
        assertNotNull(nowDateTime);
        assertEquals(nowDateTime.getZone(), ZoneId.of(timezoneId));
    }

    @Test
    public void testDate() {
        String date = Clocks.date("");
        assertNotNull(date);
        assertEquals(date, LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        String date1 = Clocks.date(dateFormat);
        assertNotNull(date1);
        assertEquals(date1, LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat)));
    }

    @Test
    public void testDateOf() {
        String dateString = "31-03-2023";
        LocalDate date = Clocks.parseDate(dateString, dateFormat);
        assertNotNull(date);
        assertEquals(date, LocalDate.of(2023, 3, 31));
    }

    @Test
    public void testDateTime() {
        String dateTime = Clocks.dateTime(dateTimeFormat);
        assertNotNull(dateTime);
        assertEquals(dateTime, LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat)));
    }

    @Test
    public void testDateTimeWithTimezone() {
        String dateTime = Clocks.dateTime(timezoneId, dateTimeZoneFormat);
        assertNotNull(dateTime);
        assertEquals(dateTime,
            ZonedDateTime.now(ZoneId.of(timezoneId)).format(DateTimeFormatter.ofPattern(dateTimeZoneFormat)));
    }

    @Test
    public void testDateTimeOf_LocalDateTime() {
        String dateTime = "2023-03-31 09:15:30";
        var expectedDateTime = LocalDateTime.of(2023, 3, 31, 9, 15, 30);
        var actualDateTime = Clocks.parseDateTime(dateTime, dateTimeFormat);
        assertEquals(actualDateTime, expectedDateTime);
    }

    @Test
    public void testDateTimeOf_ZonedDateTime() {
        String dateTimeZone = "16/07/2017 19:28:33 America/New_York";
        var expectedDateTime = ZonedDateTime.of(2017, 7, 16, 19, 28, 33, 0, ZoneId.of("America/New_York"));
        var actualDateTime = Clocks.parseDateTimeZone(dateTimeZone, dateTimeZoneFormat);
        assertEquals(actualDateTime, expectedDateTime);
    }

    @Test
    public void testDifference() {
        var start = LocalDateTime.of(2023, 3, 30, 10, 0);
        var end = LocalDateTime.of(2023, 3, 31, 14, 30);

        long daysDiff = Clocks.difference(start, end, "days");
        assertEquals(daysDiff, 1);
        long hoursDiff = Clocks.difference(start, end, "hours");
        assertEquals(hoursDiff, 28);
        long minutesDiff = Clocks.difference(start, end, "minutes");
        assertEquals(minutesDiff, 1710);
        long secondsDiff = Clocks.difference(start, end, "seconds");
        assertEquals(secondsDiff, 102600);
    }

    @Test
    public void testLastDayOfMonth() {
        var inputDate = LocalDate.of(2022, 3, 7);
        var expectedDate = LocalDate.of(2022, 3, 31);
        var actualDate = Clocks.lastDayOfMonth(inputDate);
        assertEquals(actualDate, expectedDate);
    }
}
