/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.properties.LinkedProperties;
import io.github.selcukes.commons.properties.PropertiesMapper;
import io.github.selcukes.commons.properties.SelcukesTestProperties;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.Map;

import static io.github.selcukes.commons.properties.SelcukesTestProperties.FEATURES;
import static org.testng.Assert.*;

public class PropertiesTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static String PROPS_FILE = "target/temp.properties";

    @Test
    public void createPropertyFileTest() {
        Map<String, String> data = Map.of("userName", "QA",
            "Time", DateHelper.get().timeStamp());
        Path filePath = Path.of("target/temp.properties");
        PropertiesMapper.write(filePath.toAbsolutePath().toString(), data);
        assertTrue(filePath.toFile().exists());
    }

    @Test(dependsOnMethods = "createPropertyFileTest")
    public void updatePropertyFileTest() {
        PropertiesMapper.updateProperty(PROPS_FILE, "userName", "Master");
        final Map<String, String> propertiesMap = PropertiesMapper.readAsMap(PROPS_FILE);
        assertEquals(propertiesMap.get("userName"), "Master");
        propertiesMap.forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void linkedPropertiesTest() {
        LinkedProperties linkedProperties = new LinkedProperties();
        assertFalse(linkedProperties.contains("value1"));
        linkedProperties.put("key1", "value1");
        assertTrue(linkedProperties.contains("value1"));
        linkedProperties.put("key2", "value2");
        assertTrue(linkedProperties.containsKey("key2"));
        assertEquals(linkedProperties.entrySet().size(), 2);
        linkedProperties.clear();
        assertEquals(linkedProperties.entrySet().size(), 0);
        linkedProperties.elements();
    }

    @Test
    public void selcukesTestPropsTest() {
        SelcukesTestProperties properties = new SelcukesTestProperties();
        logger.info(() -> properties.getSubstitutedConfigProperty(FEATURES));
    }
}
