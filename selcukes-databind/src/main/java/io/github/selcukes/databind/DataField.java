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

package io.github.selcukes.databind;

import io.github.selcukes.databind.annotation.Column;
import io.github.selcukes.databind.converters.Converter;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static io.github.selcukes.databind.utils.Reflections.newInstance;
import static io.github.selcukes.databind.utils.Reflections.setField;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class DataField<T> {
    private final Field field;
    @Getter
    private final Converter<T> converter;
    private final List<Converter<T>> defaultConverters;
    @Setter
    private T convertedValue;

    public DataField(
        final Field field,
        final List<Converter<T>> defaultConverters
    ) {
        this.field = field;
        this.defaultConverters = defaultConverters;
        this.converter = findMatchingConverter();

    }

    public <R> void assignValue(final R instance) {
        ofNullable(convertedValue)
            .ifPresent(value -> setField(instance, getFieldName(), value));
    }

    public String getFieldName() {
        return field.getName();
    }

    public Type getFieldType() {
        return field.getType();
    }

    public Optional<Column> getColumn() {
        return ofNullable(field.getDeclaredAnnotation(Column.class));
    }

    @SuppressWarnings("unchecked")
    private Converter<T> findMatchingConverter() {
        return getColumn()
            .map(Column::converter)
            .map(converterClass -> (Converter<T>) newInstance(converterClass))
            .filter(converterInstance -> converterInstance.getType().equals(getFieldType()))
            .orElseGet(() -> defaultConverters.stream()
                .filter(converterInstance -> converterInstance.getType().equals(getFieldType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(format(
                    "There's no matching converter found for %s field of type %s", getFieldName(), getFieldType()))
                )
            );
    }
}
