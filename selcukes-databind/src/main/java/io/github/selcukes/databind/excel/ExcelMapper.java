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
import io.github.selcukes.databind.collections.Maps;
import io.github.selcukes.databind.collections.Streams;
import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.DataFileHelper;
import io.github.selcukes.databind.utils.Resources;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is an Excel mapper to parse Excel Sheet to stream of entityClass
 * objects
 */
@UtilityClass
public class ExcelMapper {

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
        return excelMapper.parse(dataFile.getPath(fileName));
    }

    /**
     * This method takes a file path as input, creates a workbook from the file
     * at the path, and then creates a map of sheet names to a list of maps of
     * column names to cell values. The first row of each sheet is assumed to
     * contain column headers,and is skipped in the output. The remaining rows
     * are parsed and stored in the output map.
     *
     * @param  filePath            The file path of the Excel file to be parsed.
     * @return                     A map of sheet names to a DataTable of column
     *                             names to cell values.
     * @throws DataMapperException If there is an error parsing the Excel file.
     */
    public static Map<String, DataTable<String, String>> parse(String filePath) {
        try (var workbook = WorkbookFactory.create(Resources.fileStream(filePath))) {
            ExcelCell.setFormulaEvaluator(workbook.getCreationHelper().createFormulaEvaluator());

            return Streams.of(workbook.iterator())
                    .collect(Maps.of(Sheet::getSheetName, sheet -> Streams.of(sheet.iterator())
                            .skip(1)
                            .map(ExcelMapper::readRow)
                            .collect(Collectors.toCollection(DataTable::new))));
        } catch (Exception e) {
            throw new DataMapperException("Unable to parse Excel file " + filePath, e);
        }

    }

    private static Map<String, String> readRow(Row row) {
        return Streams.of(row.cellIterator())
                .collect(Maps.of(cell -> cell.getSheet().getRow(0)
                        .getCell(cell.getColumnIndex()).getStringCellValue(),
                    ExcelCell::getCellData));
    }
}
