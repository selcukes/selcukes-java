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

import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.DataFileHelper;
import io.github.selcukes.databind.utils.Streams;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is an Excel mapper to parse Excel Sheet to stream of entityClass
 * objects
 */
@UtilityClass
public class ExcelMapper {
    private static final DataFormatter DATA_FORMATTER = new DataFormatter();
    private static FormulaEvaluator formulaEvaluator;

    /**
     * Parses the Excel file to an Entity Class. It takes a class as input and
     * returns a stream of objects of that class
     *
     * @param  <T>         the Class type.
     * @param  entityClass The class of the entity to be parsed
     * @return             the Stream of Entity class objects
     */
    public <T> Stream<T> parse(final Class<T> entityClass) {
        final DataFileHelper<T> dataFile = DataFileHelper.getInstance(entityClass);
        final String fileName = dataFile.getFileName();
        int extensionIndex = fileName.lastIndexOf('.');
        final String extension = fileName.substring(extensionIndex + 1);
        if (!extension.equalsIgnoreCase("xlsx")) {
            throw new DataMapperException(String.format("File [%s] not found.",
                fileName.substring(0, extensionIndex) + ".xlsx"));
        }
        ExcelParser<T> excelMapper = new ExcelParser<>(entityClass);
        return excelMapper.parse(dataFile.getPath());
    }

    /**
     * It creates a workbook from the file, creates a formula evaluator, and
     * then creates a map of sheet names to a list of maps of column names to
     * cell values
     *
     * @param  file The file to be parsed.
     * @return      A map of sheet names to a list of maps of column names to
     *              cell values.
     */
    public static Map<String, List<Map<String, String>>> parse(File file) {
        try (var workbook = WorkbookFactory.create(new FileInputStream(file))) {
            formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            return Streams.of(workbook.iterator())
                    .collect(Collectors.toMap(Sheet::getSheetName, sheet -> Streams.of(sheet.iterator())
                            .skip(1)
                            .map(ExcelMapper::readRow)
                            .collect(Collectors.toList()),
                        (k, v) -> k, LinkedHashMap::new));
        } catch (Exception e) {
            throw new DataMapperException("Unable to parse Excel file " + file.getAbsolutePath(), e);
        }

    }

    private static Map<String, String> readRow(Row row) {
        return Streams.of(row.iterator())
                .collect(Collectors.toMap(cell -> cell.getSheet().getRow(0)
                        .getCell(cell.getColumnIndex()).getStringCellValue(),
                    cell -> {
                        if (cell.getCellType().equals(CellType.FORMULA)) {
                            return DATA_FORMATTER.formatCellValue(cell, formulaEvaluator);
                        } else {
                            return DATA_FORMATTER.formatCellValue(cell);
                        }
                    }, (k, v) -> k, LinkedHashMap::new));
    }
}
