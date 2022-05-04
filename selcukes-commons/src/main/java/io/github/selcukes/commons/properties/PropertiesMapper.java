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

package io.github.selcukes.commons.properties;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.ConfigurationException;
import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@CustomLog
@UtilityClass
public class PropertiesMapper {

    public static Map<String, String> readAsMap(final String propertyFile) {
        return getProperties(propertyFile).entrySet().stream()
            .collect(Collectors.toMap(propertyEntry -> (String) propertyEntry.getKey(),
                propertyEntry -> (String) propertyEntry.getValue(), (a, b) -> b, LinkedHashMap::new));
    }

    public static LinkedProperties getProperties(final String propertyFile) {
        LinkedProperties properties = new LinkedProperties();
        try {
            properties.load(ConfigFactory.getStream(propertyFile));
        } catch (IOException e) {
            throw new ConfigurationException("Could not parse property file '" + propertyFile + "'", e);
        }
        return properties;
    }

    public static void write(String propertyFile, Map<String, String> dataMap) {
        write(propertyFile, new LinkedProperties(), dataMap);
    }

    private static void write(String propertyFile, LinkedProperties linkedProperties, Map<String, String> dataMap) {
        dataMap.forEach(linkedProperties::setProperty);
        write(propertyFile, linkedProperties);
    }

    private static void write(String propertyFile, LinkedProperties properties) {
        try (OutputStream output = new FileOutputStream(propertyFile)) {
            properties.store(output, null);
        } catch (Exception e) {
            throw new ConfigurationException("Could not write property file '" + propertyFile + "'", e);
        }
    }

    public static void updateProperty(String propertyFile, String key, String value) {
        LinkedProperties properties = getProperties(propertyFile);
        properties.setProperty(key, value);
        write(propertyFile, properties);
    }

    public static void updateProperties(String propertyFile, Map<String, String> dataMap) {
        write(propertyFile, getProperties(propertyFile), dataMap);
    }
}
