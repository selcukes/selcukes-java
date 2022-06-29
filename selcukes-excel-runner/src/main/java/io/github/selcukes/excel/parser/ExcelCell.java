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

package io.github.selcukes.excel.parser;

import io.github.selcukes.excel.annotation.Column;
import io.github.selcukes.excel.converters.Converter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static io.github.selcukes.commons.helper.ReflectionHelper.newInstance;
import static io.github.selcukes.commons.helper.ReflectionHelper.setField;
import static io.github.selcukes.databind.utils.StringHelper.toFieldName;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;


class ExcelCell<T> {
    private final int index;
    private final Field field;
    private final Converter<T> converter;
    private final DataFormatter formatter;
    private final List<Converter<T>> defaultIConverters;

    private T convertedValue;

    public ExcelCell(
        final Field field,
        final Map<String, Integer> headers,
        final List<Converter<T>> defaultIConverters
    ) {
        this.field = field;
        this.defaultIConverters = defaultIConverters;
        String header = getColumn()
            .map(Column::name)
            .orElse(toFieldName(getFieldName()));
        Map<String, Integer> treeMap = new TreeMap<>(CASE_INSENSITIVE_ORDER);
        treeMap.putAll(headers);
        this.index = ofNullable(treeMap.get(header))
            .orElseThrow(() -> new IllegalArgumentException(format("Column %s not found", field.getName())));
        this.converter = findMatchingConverter();
        this.formatter = new DataFormatter();
    }

    public ExcelCell<T> parse(final Row row) {
        var cellValue = ofNullable(row.getCell(index, RETURN_BLANK_AS_NULL))
            .map(cell -> formatter.formatCellValue(cell).trim())
            .orElse("");
        this.convertedValue = converter.convert(cellValue, getColumn().map(Column::format).orElse(""));
        return this;
    }

    public <R> void assignValue(final R instance) {
        ofNullable(convertedValue)
            .ifPresent(value -> setField(instance, getFieldName(), value));
    }

    private String getFieldName() {
        return field.getName();
    }

    private Type getFieldType() {
        return field.getType();
    }

    private Optional<Column> getColumn() {
        return ofNullable(field.getDeclaredAnnotation(Column.class));
    }

    @SuppressWarnings("unchecked")
    private Converter<T> findMatchingConverter() {
        return getColumn()
            .map(Column::converter)
            .map(converterClass -> (Converter<T>) newInstance(converterClass))
            .filter(converterInstance -> converterInstance.getType().equals(getFieldType()))
            .orElseGet(() -> defaultIConverters.stream()
                .filter(converterInstance -> converterInstance.getType().equals(getFieldType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(format(
                    "There's no matching converter found for %s field of type %s", getFieldName(), getFieldType()))
                )
            );
    }
}

