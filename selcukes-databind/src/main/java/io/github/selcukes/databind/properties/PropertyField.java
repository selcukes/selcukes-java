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

package io.github.selcukes.databind.properties;

import io.github.selcukes.collections.Clocks;
import io.github.selcukes.databind.DataField;
import io.github.selcukes.databind.annotation.Key;
import io.github.selcukes.databind.converters.Converter;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static io.github.selcukes.collections.Reflections.getFieldValue;

class PropertyField<T> extends DataField<T> {
    private final Properties properties;

    public PropertyField(
            final Field field,
            final Properties properties,
            final List<Converter<T>> defaultConverters
    ) {
        super(field, defaultConverters);
        this.properties = properties;
    }

    public PropertyField<T> parse() {
        String keyName = getKeyName();
        var format = getFormat();
        var substituted = getSubstitutor().replace(properties, keyName, format);
        setConvertedValue(getConverter().convert(substituted, format));
        return this;
    }

    public String getKeyName() {
        return getColumn()
                .map(Key::name)
                .orElse(getFieldName());
    }

    public String getFormat() {
        return getColumn()
                .map(Key::format)
                .orElse("");
    }

    public String getFormattedValue(Object object) {
        var format = getFormat();
        var value = getFieldValue(object, getFieldName());
        if (value instanceof Temporal) {
            return Clocks.format((Temporal) value, format);
        } else if (value instanceof List<?> list) {
            return list.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        } else {
            return value.toString();
        }
    }
}
