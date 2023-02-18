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

package io.github.selcukes.commons.logging;

import io.github.selcukes.commons.helper.ExceptionHelper;
import io.github.selcukes.databind.utils.Clocks;

import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

public class SelcukesLoggerFormatter extends Formatter {
    private static final Map<Level, String> LOG_LEVELS = Map.of(
        Level.SEVERE, "ERROR",
        Level.FINE, "DEBUG",
        Level.FINER, "TRACE");

    public String format(LogRecord logRecord) {
        Throwable throwable = logRecord.getThrown();
        String throwableString = throwable != null ? String.format("%n%s", ExceptionHelper.getStackTrace(throwable))
                : "";

        return String.format("%s - [%s] - [%s.%s:%s] - %s%s%n",
            Clocks.timeStamp(logRecord.getMillis()),
            getLevel(logRecord),
            logRecord.getSourceClassName(),
            logRecord.getSourceMethodName(),
            getLineNumber(logRecord),
            formatMessage(logRecord),
            throwableString);
    }

    private String getLevel(LogRecord logRecord) {
        var level = logRecord.getLevel();
        return LOG_LEVELS.getOrDefault(level, level.getName());
    }

    private String getLineNumber(LogRecord logRecord) {
        return Stream.of(new Throwable().getStackTrace())
                .filter(frame -> frame.getClassName().equals(logRecord.getSourceClassName()))
                .findFirst()
                .map(StackTraceElement::getLineNumber)
                .map(String::valueOf)
                .orElse("");
    }

}
