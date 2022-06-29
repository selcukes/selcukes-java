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


import io.github.selcukes.commons.helper.CollectionUtils;
import io.github.selcukes.databind.annotation.DataFile;
import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.excel.converters.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.selcukes.commons.helper.ReflectionHelper.newInstance;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class ExcelData<T> {

    private final Class<T> entityClass;
    private final List<Converter<T>> defaultConverters;

    public ExcelData(final Class<T> entityClass) {
        this.entityClass = entityClass;
        this.defaultConverters = defaultConverters();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<Converter<T>> defaultConverters() {
        return Stream.of(
                BooleanConverter.class,
                StringConverter.class,
                IntegerConverter.class,
                DoubleConverter.class,
                LocalDateConverter.class,
                LocalDateTimeConverter.class
            )
            .map(cls -> (Converter<T>) newInstance(cls))
            .collect(Collectors.toList());
    }

    public Stream<T> parse(Path filePath) {
        try (var workbook = WorkbookFactory.create(new FileInputStream(filePath.toFile()))) {
            var formatter = new DataFormatter();
            var startIndex = 0;
            var skip = 1;

            var sheet = ofNullable(entityClass.getDeclaredAnnotation(DataFile.class))
                .map(annotation -> workbook.getSheet(annotation.sheetName()))
                .orElse(workbook.getSheetAt(startIndex));

            var headers = CollectionUtils.toStream(sheet.getRow(startIndex).cellIterator())
                .collect(Collectors.toMap(cell -> formatter.formatCellValue(cell).trim(), Cell::getColumnIndex));

            var cellMappers = Stream.of(entityClass.getDeclaredFields())
                .map(field -> new ExcelCell<>(field, headers, defaultConverters))
                .collect(Collectors.toList());

            return CollectionUtils.toStream(sheet.iterator())
                .skip(skip)
                .map(row -> cellMappers.stream().map(cellMapper -> cellMapper.parse(row)).collect(Collectors.toList()))
                .map(this::initEntity);
        } catch (Exception ex) {
            throw new DataMapperException(format("Unable to parse Excel data to %s.", entityClass), ex);
        }
    }

    public T initEntity(final List<ExcelCell<T>> mappers) {
        var hasDefaultConstructor = Stream.of(entityClass.getDeclaredConstructors())
            .anyMatch(constructor -> constructor.getParameterCount() == 0);
        if (!hasDefaultConstructor) {
            throw new IllegalStateException(format("%s must have default constructor.", entityClass.getSimpleName()));
        }

        var entity = newInstance(entityClass);
        mappers.forEach(mapper -> mapper.assignValue(entity));

        return entity;
    }
}

