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

package io.github.selcukes.core.logging;

import io.github.selcukes.core.helper.Preconditions;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

public final class LogRecordListener {

    private final ConcurrentLinkedDeque<LogRecord> logRecords = new ConcurrentLinkedDeque<>();

    void logRecordSubmitted(LogRecord logRecord) {
        this.logRecords.add(logRecord);
    }

    public Stream<LogRecord> getLogStream() {
        return this.logRecords.stream();
    }

    public Stream<LogRecord> getLogStream(Level level) {
        Preconditions.checkNotNull(level, "Level must not be null");
        return getLogStream().filter(logRecord -> logRecord.getLevel() == level);
    }
}
