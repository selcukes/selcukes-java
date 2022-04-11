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

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ExcelReader {
    private final Workbook workbook;

    public ExcelReader(String fileName) {
        this.workbook = getWorkBook(fileName);
    }

    @SneakyThrows
    private Workbook getWorkBook(String filePath) {
        return WorkbookFactory.create(new File(filePath));
    }

    public List<Sheet> getAllSheets() {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(workbook.iterator(), Spliterator.ORDERED), false)
            .collect(Collectors.toList());
    }

    public List<List<String>> getSheetData(Sheet sheet) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(sheet.iterator(), Spliterator.ORDERED), false)
            .map(this::getRowData)
            .collect(Collectors.toList());
    }

    public List<String> getRowData(Row row) {
        DataFormatter df = new DataFormatter();
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        return StreamSupport
            .stream(Spliterators.spliteratorUnknownSize(row.iterator(), Spliterator.ORDERED), false)
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

