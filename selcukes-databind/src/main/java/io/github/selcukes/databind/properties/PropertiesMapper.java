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

package io.github.selcukes.databind.properties;

import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.DataFileHelper;
import io.github.selcukes.databind.utils.Maps;
import lombok.experimental.UtilityClass;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;


/**
 * The type Properties mapper.
 */
@UtilityClass
public class PropertiesMapper {
    /**
     * Parses the Properties file to an Entity Class.
     *
     * @param <T>         the Class type.
     * @param entityClass the entity class
     * @return the Entity class objects
     */
    public static <T> T parse(final Class<T> entityClass) {
        final DataFileHelper<T> dataFile = DataFileHelper.getInstance(entityClass);
        final String fileName = dataFile.getFileName();
        int extensionIndex = fileName.lastIndexOf('.');
        final String extension = fileName.substring(extensionIndex + 1);
        if (!extension.equalsIgnoreCase("properties")) {
            throw new DataMapperException(String.format("File [%s] not found.",
                fileName.substring(0, extensionIndex) + ".properties"));
        }
        PropertiesParser<T> propertiesParser = new PropertiesParser<>(entityClass);
        return propertiesParser.parse(dataFile.getPath());
    }

    /**
     * Parses the Properties file to a map.
     *
     * @param propertyFile the property file
     * @return the map
     */
    public static Map<String, String> parse(final String propertyFile) {
        return Maps.of(PropertiesLoader.linkedProperties(propertyFile));
    }

    /**
     * Writes a Properties file.
     *
     * @param propertyFile the property file
     * @param dataMap      the data map
     */
    public static void write(final String propertyFile, Map<String, String> dataMap) {
        write(propertyFile, new LinkedProperties(), dataMap);
    }

    private static void write(final String propertyFile, LinkedProperties linkedProperties, Map<String, String> dataMap) {
        dataMap.forEach(linkedProperties::setProperty);
        write(propertyFile, linkedProperties);
    }

    private static void write(final String propertyFile, LinkedProperties properties) {
        try (OutputStream output = new FileOutputStream(propertyFile)) {
            properties.store(output, null);
        } catch (Exception e) {
            throw new DataMapperException("Could not write property file '" + propertyFile + "'", e);
        }
    }

    /**
     * Update Property file.
     *
     * @param propertyFile the property file
     * @param key          the key
     * @param value        the value
     */
    public static void updateProperty(final String propertyFile, String key, String value) {
        LinkedProperties properties = PropertiesLoader.linkedProperties(propertyFile);
        properties.setProperty(key, value);
        write(propertyFile, properties);
    }

    /**
     * Update properties.
     *
     * @param propertyFile the property file
     * @param dataMap      the data map
     */
    public static void updateProperties(final String propertyFile, Map<String, String> dataMap) {
        write(propertyFile, PropertiesLoader.linkedProperties(propertyFile), dataMap);
    }
}
