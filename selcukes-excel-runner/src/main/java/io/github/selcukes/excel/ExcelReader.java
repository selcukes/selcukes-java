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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExcelReader {

    private final String fileName;
    private Workbook workbook;

    public ExcelReader(String fileName) {
        this.fileName = fileName;
    }

    @SneakyThrows
    private Workbook getWorkBook(String filePath) {
        return WorkbookFactory.create(new File(filePath));
    }

    private Sheet getWorkBookSheet(String fileName, String sheetName) {
        this.workbook = getWorkBook(fileName);
        return this.workbook.getSheet(sheetName);
    }

    public List<Sheet> getAllSheets() {
        List<Sheet> sheetNames = new ArrayList<>();
        this.workbook = getWorkBook(fileName);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheetNames.add(workbook.getSheetAt(i));
        }
        return sheetNames;
    }

    @SneakyThrows
    public List<List<String>> getSheetData(String sheetName) {
        Sheet sheet;
        List<List<String>> outerList;
        try {
            sheet = getWorkBookSheet(fileName, sheetName);
            outerList = getSheetData(sheet);
        } finally {
            this.workbook.close();
        }
        return outerList;
    }

    public List<List<String>> getSheetData(Sheet sheet) {
        List<List<String>> outerList = new LinkedList<>();
        prepareOuterList(sheet, outerList);
        return outerList;
    }

    private void prepareOuterList(Sheet sheet, List<List<String>> outerList) {

        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            List<String> innerList = new LinkedList<>();
            Row row = sheet.getRow(i);
            for (int j = sheet.getRow(0).getFirstCellNum(); j < sheet.getRow(0).getLastCellNum(); j++) {
                prepareInnerList(innerList, row, j);
            }
            outerList.add(innerList);
        }
    }

    private void prepareInnerList(List<String> innerList, Row row, int j) {
        Cell cell = row.getCell(j);
        DataFormatter df = new DataFormatter();
        if (cell == null) {
            innerList.add("");
        } else if (cell.getCellType().equals(CellType.FORMULA)) {
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            innerList.add(df.formatCellValue(cell, formulaEvaluator));
        } else {
            innerList.add(df.formatCellValue(cell));
        }
    }
}

