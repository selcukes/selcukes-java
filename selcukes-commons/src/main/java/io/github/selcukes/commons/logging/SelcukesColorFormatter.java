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

package io.github.selcukes.commons.logging;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class SelcukesColorFormatter extends SelcukesLoggerFormatter {
    private static final String COLOR_RESET = "\u001b[0m";
    private static final String COLOR_SEVERE = "\u001b[91m";
    private static final Map<Level, String> LEVEL_COLORS = Map.of(
        Level.SEVERE, COLOR_SEVERE,
        Level.WARNING, "\u001b[93m",
        Level.INFO, "\u001b[32m",
        Level.CONFIG, "\u001b[94m",
        Level.FINE, "\u001b[36m",
        Level.FINER, "\u001b[35m",
        Level.FINEST, "\u001b[90m");

    private String getColoredMessage(final Level level, final String message) {
        String prefix = LEVEL_COLORS.getOrDefault(level, COLOR_SEVERE);
        return prefix + message + COLOR_RESET;
    }

    @Override
    public String format(LogRecord logRecord) {
        String message = super.format(logRecord);
        return getColoredMessage(logRecord.getLevel(), message);
    }
}
