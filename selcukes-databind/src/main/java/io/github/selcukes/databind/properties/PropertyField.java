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

import io.github.selcukes.databind.DataField;
import io.github.selcukes.databind.annotation.Key;
import io.github.selcukes.databind.converters.Converter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

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
        String keyName = getColumn()
            .map(Key::name)
            .orElse(getFieldName());
        var substituted = getSubstitutor().replace(properties, keyName);
        setConvertedValue(getConverter().convert(substituted));
        return this;
    }

}
