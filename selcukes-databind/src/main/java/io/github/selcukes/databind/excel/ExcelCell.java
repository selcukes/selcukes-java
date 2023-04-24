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

import io.github.selcukes.databind.DataField;
import io.github.selcukes.databind.annotation.Key;
import io.github.selcukes.databind.converters.Converter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;

class ExcelCell<T> extends DataField<T> {
    private static final ThreadLocal<DataFormatter> DATA_FORMATTER = ThreadLocal.withInitial(DataFormatter::new);
    private static final ThreadLocal<FormulaEvaluator> FORMULA_EVALUATOR = new ThreadLocal<>();
    private final int index;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ExcelCell::cleanup));
    }

    public ExcelCell(
            final Field field,
            final Map<String, Integer> headers,
            final List<Converter<T>> defaultConverters
    ) {
        super(field, defaultConverters);
        this.index = getIndex(headers);
    }

    public ExcelCell<T> parse(final Row row) {
        var cellValue = ofNullable(row.getCell(index, RETURN_BLANK_AS_NULL))
                .map(ExcelCell::getCellData)
                .orElse("");
        var format = getColumn().map(Key::format).orElse("");
        var substituted = getSubstitutor().replace(cellValue, format);
        var converted = getConverter().convert(substituted, format);
        setConvertedValue(converted);
        return this;
    }

    private int getIndex(Map<String, Integer> headers) {
        String header = getColumn()
                .map(Key::name)
                .orElse(getFieldName());
        return ofNullable(headers.get(header))
                .orElseThrow(() -> new IllegalArgumentException(format("Column %s not found", getFieldName())));
    }

    public static void setFormulaEvaluator(FormulaEvaluator formulaEvaluator) {
        FORMULA_EVALUATOR.set(formulaEvaluator);
    }

    protected static String getCellData(Cell cell) {
        var cellData = cell.getCellType().equals(CellType.FORMULA)
                ? DATA_FORMATTER.get().formatCellValue(cell, FORMULA_EVALUATOR.get())
                : DATA_FORMATTER.get().formatCellValue(cell);
        return cellData.trim();
    }

    private static void cleanup() {
        if (DATA_FORMATTER.get() != null) {
            DATA_FORMATTER.remove();
        }
        if (FORMULA_EVALUATOR.get() != null) {
            FORMULA_EVALUATOR.remove();
        }
    }
}
