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

package io.github.selcukes.databind.excel;

import io.github.selcukes.databind.annotation.Column;
import io.github.selcukes.databind.converters.Converter;
import io.github.selcukes.databind.DataField;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static io.github.selcukes.databind.utils.StringHelper.toFieldName;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;


class ExcelCell<T> extends DataField<T> {
    private final int index;
    private final DataFormatter formatter;

    public ExcelCell(
        final Field field,
        final Map<String, Integer> headers,
        final List<Converter<T>> defaultConverters
    ) {
        super(field, defaultConverters);
        this.index = getIndex(headers);
        this.formatter = new DataFormatter();
    }

    public ExcelCell<T> parse(final Row row) {
        var cellValue = ofNullable(row.getCell(index, RETURN_BLANK_AS_NULL))
            .map(cell -> formatter.formatCellValue(cell).trim())
            .orElse("");
        setConvertedValue(getConverter().convert(cellValue, getColumn().map(Column::format).orElse("")));
        return this;
    }

    private int getIndex(Map<String, Integer> headers) {
        String header = getColumn()
            .map(Column::name)
            .orElse(toFieldName(getFieldName()));
        Map<String, Integer> headersMap = new TreeMap<>(CASE_INSENSITIVE_ORDER);
        headersMap.putAll(headers);
        return ofNullable(headersMap.get(header))
            .orElseThrow(() -> new IllegalArgumentException(format("Column %s not found", getFieldName())));
    }
}

