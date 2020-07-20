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

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelcukesRuntimeAdapter implements SelcukesRuntimeOptions {
    private final Logger logger = LoggerFactory.getLogger(SelcukesRuntimeAdapter.class);
    private static SelcukesRuntimeOptions runtimeOptions;

    public static SelcukesRuntimeOptions getInstance() {
        if (runtimeOptions == null)
            runtimeOptions = new SelcukesRuntimeAdapter();
        return runtimeOptions;
    }

    @Override
    public void perform() {
        final Map<String, String> properties = ConfigFactory
            .loadPropertiesMap("selcukes-test.properties");

        UUID uuid = UUID.randomUUID();
        String features = properties.getOrDefault("selcukes.features", "");
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(features);
        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, properties.getOrDefault(matcher.group(1), ""));
        }
        matcher.appendTail(stringBuffer);
        features = stringBuffer.toString();

        String glue = properties.getOrDefault("selcukes.glue", "");
        String tag = properties.getOrDefault("selcukes.tags", "");
        String reportsPath = properties.getOrDefault("selcukes.reports-path", "target/cucumber-reports");

        String plugin = "pretty, html:" + reportsPath + ", json:" + reportsPath + "/cucumber" + uuid + ".json";

        System.setProperty("cucumber.features", features);
        System.setProperty("cucumber.filter.tags", tag);
        System.setProperty("cucumber.glue", glue);
        System.setProperty("cucumber.plugin", plugin);
        String finalFeatures = features;
        logger.debug(() -> String.format("Using Runtime Cucumber Options:\nFeatures : [%s]\nGlue     : [%s]\nTags     : [%s] " +
            "\n ", finalFeatures, glue, tag));
    }
}
