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
 * > This class is a data mapper to parse or write data files
 */
@UtilityClass
public class DataMapper {

    /**
     * Parses the data file according to POJO Class.
     * It takes a class, gets the file name, gets the extension, looks up the extension in the map, and then parses the
     * file
     *
     * @param entityClass The class of the entity to be parsed.
     * @return A generic object of type T
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
     * "Write the given entity value to a file with the given extension."
     * <p>
     * The first thing we do is get the DataFileHelper for the given value. This is a class that knows how to read and
     * write the given value to a file
     *
     * @param value The object to be written to the file.
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
     * This function takes a JSON string and an entity class, and returns an instance of the class with the data from the JSON
     * string.
     *
     * @param content     The JSON string to be parsed.
     * @param entityClass The class of the entity to be parsed.
     * @return A new instance of the entity class with the content parsed into it.
     */
    @SneakyThrows
    public <T> T parse(final String content, final Class<T> entityClass) {
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
