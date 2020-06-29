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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

public final class LogRecordListener {
    private final ThreadLocal<List<LogRecord>> logRecords = ThreadLocal.withInitial(ArrayList::new);

    void logRecordSubmitted(LogRecord logRecord) {
        this.logRecords.get().add(logRecord);
    }

    public Stream<LogRecord> getLogRecords() {
        return this.logRecords.get().stream();
    }

    public Stream<LogRecord> getLogRecords(Level level) {
        Preconditions.checkNotNull(level, "Level must not be null");
        return getLogRecords().filter(logRecord -> logRecord.getLevel() == level);
    }
    public void cleanUp()
    {
        logRecords.remove();
    }
}
