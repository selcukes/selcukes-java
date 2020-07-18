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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class PropertiesFileLoaderTest {
    @Test
    public void loadPropertiesMapTest() {
        final LinkedHashMap<String, String> propertiesMap = ConfigFactory.loadPropertiesMap();
        Assert.assertEquals(propertiesMap.size(), 2);
        Assert.assertEquals(propertiesMap.get("test.env"),"QA");
        Assert.assertEquals(propertiesMap.get("test.browser"),"CHROME");
    }
}
