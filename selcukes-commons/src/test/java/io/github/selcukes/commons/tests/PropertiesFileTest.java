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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.properties.LinkedProperties;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class PropertiesFileTest {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void loadPropertiesMapTest() {
        final Map<String, String> propertiesMap = ConfigFactory.loadPropertiesMap("selcukes-test.properties");
        Assert.assertEquals(propertiesMap.size(), 2);
        Assert.assertEquals(propertiesMap.get("test.env"), "QA");
        Assert.assertEquals(propertiesMap.get("test.browser"), "CHROME");
        propertiesMap.forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void linkedPropertiesTest() {
        LinkedProperties linkedProperties = new LinkedProperties();
        Assert.assertFalse(linkedProperties.contains("value1"));
        linkedProperties.put("key1", "value1");
        Assert.assertTrue(linkedProperties.contains("value1"));
        linkedProperties.put("key2", "value2");
        Assert.assertTrue(linkedProperties.containsKey("key2"));
        Assert.assertEquals(linkedProperties.entrySet().size(), 2);
        linkedProperties.clear();
        Assert.assertEquals(linkedProperties.entrySet().size(), 0);
        linkedProperties.elements();
    }
}
