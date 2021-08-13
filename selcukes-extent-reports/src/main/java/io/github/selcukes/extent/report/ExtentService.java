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

package io.github.selcukes.extent.report;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.observer.ExtentObserver;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.ReporterConfigurable;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import io.github.selcukes.commons.helper.DateHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class ExtentService implements Serializable {

    private static final long serialVersionUID = -5008231199972325650L;

    private static Properties properties;

    public static synchronized ExtentReports getInstance() {
        return ExtentReportsLoader.INSTANCE;
    }

    public static Object getProperty(String key) {
        String sys = System.getProperty(key);
        return sys == null ? (properties == null ? null : properties.get(key)) : sys;
    }


    @SuppressWarnings("unused")
    private ExtentReports readResolve() {
        return ExtentReportsLoader.INSTANCE;
    }

    private static class ExtentReportsLoader {
        private static final String THUMBNAIL_REPORT = "thumbnail.report";
        private static final String TIMESTAMP_REPORT = "timestamp.report";
        private static final ExtentReports INSTANCE = new ExtentReports();
        private static final String[] DEFAULT_SETUP_PATH = new String[]{"extent.properties",
            "com/aventstack/adapter/extent.properties"};
        private static final String SYS_INFO_MARKER = "systeminfo.";
        private static final String OUTPUT_PATH = "target/";
        private static final String EXTENT_REPORTER = "extent.reporter";
        private static final String START = "start";
        private static final String CONFIG = "config";
        private static final String OUT = "out";
        private static final String VIEW_ORDER = "vieworder";
        private static final String DELIM = ".";
        private static final String SPARK = "spark";

        private static final String INIT_SPARK_KEY = EXTENT_REPORTER + DELIM + SPARK + DELIM + START;
        private static final String CONFIG_SPARK_KEY = EXTENT_REPORTER + DELIM + SPARK + DELIM + CONFIG;
        private static final String OUT_SPARK_KEY = EXTENT_REPORTER + DELIM + SPARK + DELIM + OUT;

        private static final String VIEW_ORDER_SPARK_KEY = EXTENT_REPORTER + DELIM + SPARK + DELIM + VIEW_ORDER;
        private static final String REPORT_NAME = "Automation Report";
        private static final String REPORT_TITLE = "Automation Report";

        static {
            createViaProperties();
            createViaSystem();
        }

        private static void createViaProperties() {

            ClassLoader loader = ExtentReportsLoader.class.getClassLoader();
            Optional<InputStream> is = Arrays.stream(DEFAULT_SETUP_PATH).map(loader::getResourceAsStream)
                .filter(Objects::nonNull).findFirst();
            if (is.isPresent()) {
                Properties properties = new Properties();
                try {
                    properties.load(is.get());
                    ExtentService.properties = properties;

                    if (properties.containsKey(INIT_SPARK_KEY)
                        && "true".equals(String.valueOf(properties.get(INIT_SPARK_KEY))))
                        initSpark(properties);


                    addSystemInfo(properties);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private static void createViaSystem() {
            initSpark(null);
            addSystemInfo(System.getProperties());
        }

        private static String getOutputPath(Properties properties, String key) {
            String out;
            if (properties != null && properties.get(key) != null)
                out = String.valueOf(properties.get(key));
            else
                out = System.getProperty(key);
            out = out == null || out.equals("null") || out.isEmpty() ? OUTPUT_PATH + key.split("\\.")[2] + "/" : out;
            return out;
        }

        private static boolean isTimeStampReport() {
            if (properties != null && properties.get(TIMESTAMP_REPORT) != null)
                return String.valueOf(properties.get(TIMESTAMP_REPORT)).equalsIgnoreCase("true");
            return false;
        }
        private static boolean isThumbnailReport() {
            if (properties != null && properties.get(THUMBNAIL_REPORT) != null)
                return String.valueOf(properties.get(THUMBNAIL_REPORT)).equalsIgnoreCase("true");
            return false;
        }

        private static void initSpark(Properties properties) {
            String out = getOutputPath(properties, OUT_SPARK_KEY);
            if (isTimeStampReport()) {
                out = out.replace(".", DateHelper.get().dateTime() + ".");
            }
            ExtentSparkReporter spark = new ExtentSparkReporter(out);
            spark.config().setReportName(REPORT_NAME);
            spark.config().setDocumentTitle(REPORT_TITLE);
            spark.config().thumbnailForBase64(isThumbnailReport());
            sparkReportViewOrder(spark);

            attach(spark, properties);
        }

        private static void sparkReportViewOrder(ExtentSparkReporter spark) {
            try {
                List<ViewName> viewOrder = Arrays.stream(String.valueOf(getProperty(VIEW_ORDER_SPARK_KEY)).split(","))
                    .map(v -> ViewName.valueOf(v.toUpperCase())).collect(Collectors.toList());
                spark.viewConfigurer().viewOrder().as(viewOrder).apply();
            } catch (Exception e) {
                // Do nothing. Use default order.
            }
        }

        private static void attach(ReporterConfigurable r, Properties properties) {
            Object configPath = properties == null ? System.getProperty(ExtentReportsLoader.CONFIG_SPARK_KEY) : properties.get(ExtentReportsLoader.CONFIG_SPARK_KEY);
            if (configPath != null && !String.valueOf(configPath).isEmpty())
                try {
                    r.loadXMLConfig(String.valueOf(configPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            INSTANCE.attachReporter((ExtentObserver<?>) r);
        }

        private static void addSystemInfo(Properties properties) {
            properties.forEach((k, v) -> {
                String key = String.valueOf(k);
                if (key.startsWith(SYS_INFO_MARKER)) {
                    key = key.substring(key.indexOf('.') + 1);
                    INSTANCE.setSystemInfo(key, String.valueOf(v));
                }
            });
        }
    }
}

