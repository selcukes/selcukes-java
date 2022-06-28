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

package io.github.selcukes.excel.converters;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.ofNullable;

public class LocalDateTimeConverter extends DefaultConverter<LocalDateTime> {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public LocalDateTime convert(final String value) {
        return convert(value, DEFAULT_FORMAT);
    }

    @Override
    public LocalDateTime convert(final String value, final String format) {
        return parse(value, ofPattern(ofNullable(format).filter(f -> !f.isEmpty()).orElse(DEFAULT_FORMAT)));
    }
}
