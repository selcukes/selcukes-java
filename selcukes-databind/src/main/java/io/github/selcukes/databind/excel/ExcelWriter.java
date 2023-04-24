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

import io.github.selcukes.databind.collections.DataTable;
import io.github.selcukes.databind.collections.Streams;
import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.Resources;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

class ExcelWriter {

    <T> void write(
            @NonNull DataTable<String, T> dataTable,
            @NonNull Path filePath,
            @NonNull String sheetName
    ) {
        try (var inputStream = Resources.newFileStream(filePath.toString());
                var workbook = getOrCreateWorkbook(inputStream);
                var outputStream = Resources.newOutputStream(filePath)) {

            var sheet = ofNullable(workbook.getSheet(sheetName))
                    .orElseGet(() -> workbook.createSheet(sheetName));

            var columns = dataTable.getColumns();
            var headerRow = sheet.createRow(0);
            Streams.of(columns).forEach(i -> {
                var cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i));
            });
            var rows = dataTable.rows().collect(Collectors.toList());
            Streams.of(rows).forEach(i -> {
                var rowData = rows.get(i);
                var row = getOrCreateRow(sheet, i + 1);
                Streams.of(columns).forEach(j -> {
                    var column = columns.get(j);
                    var value = rowData.get(column);
                    var cell = getOrCreateCell(row, j);
                    if (value == null) {
                        cell.setBlank();
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                });
            });
            workbook.write(outputStream);
        } catch (Exception e) {
            throw new DataMapperException("Error while writing to Excel file: " + e.getMessage(), e);
        }
    }

    @SneakyThrows
    private Workbook getOrCreateWorkbook(InputStream inputStream) {
        return inputStream == null ? new XSSFWorkbook() : WorkbookFactory.create(inputStream);
    }

    private Row getOrCreateRow(@NonNull Sheet sheet, int rowIndex) {
        return ofNullable(sheet.getRow(rowIndex)).orElseGet(() -> sheet.createRow(rowIndex));
    }

    private Cell getOrCreateCell(@NonNull Row row, int columnIndex) {
        return ofNullable(row.getCell(columnIndex)).orElseGet(() -> row.createCell(columnIndex));
    }
}
