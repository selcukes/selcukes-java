/*
 *
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
 *
 */

package io.github.selcukes.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.DataFileHelper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;

/**
 * The type Data mapper.
 */
@UtilityClass
public class DataMapper {
    /**
     * Parses the data file according to POJO Class.
     *
     * @param <T>         the Class type.
     * @param entityClass the resource class
     * @return the POJO class object
     */
    public <T> T parse(final Class<T> entityClass) {
        final DataFileHelper<T> dataFile = DataFileHelper.getInstance(entityClass);
        final String fileName = dataFile.getFileName();
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        final DataBind dataBind = lookup(extension);
        return dataFile.isStream() ? dataBind.parse(dataFile.fileStream(), entityClass) :
                dataBind.parse(dataFile.getPath(), entityClass);
    }

    /**
     * Write POJO class to a file
     *
     * @param <T>   the Class type
     * @param value Object of a class or variable
     */
    @SuppressWarnings("unchecked")
    public <T> void write(final T value) {
        final DataFileHelper<T> dataFile = (DataFileHelper<T>) DataFileHelper.getInstance(value.getClass());
        dataFile.setNewFile(true);
        final String fileName = dataFile.getFileName();
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        final DataBind dataBind = lookup(extension);
        dataBind.write(dataFile.getPath(), value);
    }

    /**
     * Parse Json String to a Pojo class
     *
     * @param <T>         the type parameter
     * @param content     the content
     * @param entityClass the resource class
     * @return the t
     */
    @SneakyThrows
    public <T> T parse(String content, Class<T> entityClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(SNAKE_CASE);
        return mapper.readValue(content, entityClass);
    }

    private DataBind lookup(final String extension) {
        switch (extension.toLowerCase()) {
            case "yaml":
            case "yml":
                return new YamlData();
            case "json":
                return new JsonData();
            default:
                throw new DataMapperException(String.format("File Type[%s] not supported...", extension));
        }

    }
}
