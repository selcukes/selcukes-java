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

package io.github.selcukes.excel;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.ExcelConfigException;
import io.github.selcukes.databind.utils.Streams;
import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelReader {
    private final Workbook workbook;

    public ExcelReader(String fileName) {
        this.workbook = getWorkBook(fileName);
    }

    private Workbook getWorkBook(String fileName) {
        try (Workbook wb = WorkbookFactory.create(Objects.requireNonNull(ConfigFactory.getStream(fileName)))) {
            return wb;
        } catch (Exception e) {
            throw new ExcelConfigException("Failed reading excel file : " + fileName);
        }
    }

    public Stream<Sheet> getAllSheets() {
        return Streams.of(workbook.iterator());
    }

    public Map<String, List<List<String>>> getAllSheetsDataMap() {
        return getAllSheets().collect(Collectors.toMap(Sheet::getSheetName, this::getSheetData));
    }

    public List<List<String>> getSheetData(Sheet sheet) {
        return Streams.of(sheet.iterator())
            .map(this::getRowData)
            .collect(Collectors.toList());
    }

    private List<String> getRowData(Row row) {
        DataFormatter df = new DataFormatter();
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        return Streams.of(row.iterator())
            .map(cell -> {
                if (cell == null) {
                    return "";
                } else if (cell.getCellType().equals(CellType.FORMULA)) {
                    return df.formatCellValue(cell, formulaEvaluator);
                } else {
                    return df.formatCellValue(cell);
                }
            }).collect(Collectors.toList());
    }


}

