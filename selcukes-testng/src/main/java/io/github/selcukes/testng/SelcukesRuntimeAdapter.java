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

package io.github.selcukes.testng;

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.databind.utils.StringUtils;

import java.util.Map;
import java.util.UUID;

public class SelcukesRuntimeAdapter implements SelcukesRuntimeOptions {

    private final Logger logger = LoggerFactory.getLogger(SelcukesRuntimeAdapter.class);
    private static SelcukesRuntimeOptions runtimeOptions;
    private Map<String, String> properties;

    public static SelcukesRuntimeOptions getInstance() {
        if (runtimeOptions == null)
            runtimeOptions = new SelcukesRuntimeAdapter();
        return runtimeOptions;
    }

    @Override
    public void perform() {
        try {
            properties = ConfigFactory
                .loadPropertiesMap("selcukes-test.properties");

            UUID uuid = UUID.randomUUID();
            String features = StringUtils.replaceProperty(getProperty("selcukes.features"),
                matcher -> getProperty(matcher.group(1)));

            String glue = getProperty("selcukes.glue");
            String tag = getProperty("selcukes.tags");
            String reportsPath = getProperty("selcukes.reports-path");
            if (reportsPath.equals("")) reportsPath = "target/cucumber-reports";

            String plugin = "html:" + reportsPath + "/cucumber.html, json:" + reportsPath + "/cucumber" + uuid + ".json";

            System.setProperty("cucumber.features", features);
            System.setProperty("cucumber.filter.tags", tag);
            System.setProperty("cucumber.glue", glue);
            System.setProperty("cucumber.plugin", plugin);
            System.setProperty("cucumber.publish.quiet", "true");
            logger.debug(() -> String.format("Using Runtime Cucumber Options:\nFeatures : [%s]\nGlue     : [%s]\nTags     : [%s] " +
                "\n ", features, glue, tag));
        } catch (Exception exception) {
            logger.warn(() -> "Failed loading selcukes-test properties. Using default CucumberOptions to execute...");
        }
    }

    public String getProperty(String propertyKey) {
        if (System.getProperty(propertyKey) != null) {
            return System.getProperty(propertyKey);
        }
        return properties.getOrDefault(propertyKey, "");
    }

}
