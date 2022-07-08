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
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@UtilityClass
public class PropertiesLoader {
    public static Properties getProperties(Path filePath) {
        var properties = new Properties();
        try (InputStream stream = new FileInputStream(filePath.toFile())) {
            properties.load(stream);
        } catch (IOException e) {
            throw new DataMapperException("Could not parse property file '" + filePath.toFile().getName() + "'", e);
        }
        return properties;
    }

    public static LinkedProperties linkedProperties(String propertyFile) {
        var properties = new LinkedProperties();
        try (InputStream stream = new FileInputStream(Paths.get(propertyFile).toFile())) {
            properties.load(stream);
        } catch (IOException e) {
            throw new DataMapperException("Could not parse property file '" + propertyFile + "'", e);
        }
        return properties;
    }
}
