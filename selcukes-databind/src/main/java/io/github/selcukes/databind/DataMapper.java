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

import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.DataFileHelper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DataMapper {
    public <T> T parse(final Class<T> resourceClass) {
        final DataFileHelper<T> dataFile = DataFileHelper.getInstance(resourceClass);
        final String fileName = dataFile.getFileName();
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        final DataBind dataBind = lookup(extension);
        return dataBind.parse(dataFile.getPath(), resourceClass);
    }

    public <T> void write(final T value) {
        final DataFileHelper<T> dataFile = (DataFileHelper<T>) DataFileHelper.getInstance(value.getClass());
        dataFile.setNewFile(true);
        final String fileName = dataFile.getFileName();
        final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        final DataBind dataBind = lookup(extension);
        dataBind.write(dataFile.getPath(), value);
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
