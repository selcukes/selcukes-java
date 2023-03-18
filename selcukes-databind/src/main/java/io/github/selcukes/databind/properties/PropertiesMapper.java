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

import io.github.selcukes.databind.collections.Maps;
import io.github.selcukes.databind.exception.DataMapperException;
import io.github.selcukes.databind.utils.DataFileHelper;
import lombok.experimental.UtilityClass;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

/**
 * The type Properties mapper.
 */
@UtilityClass
public class PropertiesMapper {

    /**
     * > It parses a properties file and returns an object of the specified
     * class
     *
     * @param  entityClass The class of the entity to be parsed.
     * @return             A new instance of the class passed in.
     */
    public <T> T parse(final Class<T> entityClass) {
        final DataFileHelper<T> dataFile = DataFileHelper.getInstance(entityClass);
        final String fileName = dataFile.getFileName();
        int extensionIndex = fileName.lastIndexOf('.');
        final String extension = fileName.substring(extensionIndex + 1);
        if (!extension.equalsIgnoreCase("properties")) {
            throw new DataMapperException(String.format("File [%s] not found.",
                fileName.substring(0, extensionIndex) + ".properties"));
        }
        PropertiesParser<T> propertiesParser = new PropertiesParser<>(entityClass);
        return propertiesParser.parse(dataFile.getPath(fileName));
    }

    /**
     * It takes a property file and returns a map of the properties
     *
     * @param  propertyFile The name of the property file to parse.
     * @return              A map of the properties in the file.
     */
    public static Map<String, String> parse(final String propertyFile) {
        return Maps.of(PropertiesLoader.getProperties(Path.of(propertyFile)));
    }

    /**
     * > This function writes the dataMap to the propertyFile
     *
     * @param propertyFile The path to the property file.
     * @param dataMap      A map of key-value pairs to be written to the
     *                     property file.
     */
    public static void write(final String propertyFile, final Map<String, String> dataMap) {
        write(propertyFile, new Properties(), dataMap);
    }

    private static void write(
            final String propertyFile, final Properties linkedProperties, final Map<String, String> dataMap
    ) {
        dataMap.forEach(linkedProperties::setProperty);
        write(propertyFile, linkedProperties);
    }

    private static void write(final String propertyFile, final Properties properties) {
        try (OutputStream output = new FileOutputStream(propertyFile)) {
            properties.store(output, null);
        } catch (Exception e) {
            throw new DataMapperException("Could not write property file '" + propertyFile + "'", e);
        }
    }

    /**
     * It loads the properties file, sets the property, and writes the file back
     * to disk
     *
     * @param propertyFile The path to the properties file.
     * @param key          the key of the property you want to update
     * @param value        the value to be written to the property file
     */
    public static void updateProperty(final String propertyFile, final String key, final String value) {
        Properties properties = PropertiesLoader.getProperties(Path.of(propertyFile));
        properties.setProperty(key, value);
        write(propertyFile, properties);
    }

    /**
     * It takes a property file and a map of key-value pairs, and updates the
     * property file with the key-value pairs
     *
     * @param propertyFile The path to the properties file.
     * @param dataMap      A map of key-value pairs that you want to update in
     *                     the properties file.
     */
    public static void updateProperties(final String propertyFile, final Map<String, String> dataMap) {
        write(propertyFile, PropertiesLoader.getProperties(Path.of(propertyFile)), dataMap);
    }

    /**
     * It returns a Properties object that contains all the system properties
     * and environment variables
     *
     * @return A Properties object containing all the system properties and
     *         environment variables.
     */
    public static Properties systemProperties() {
        var properties = System.getProperties();
        properties.putAll(System.getenv());
        return properties;
    }
}
