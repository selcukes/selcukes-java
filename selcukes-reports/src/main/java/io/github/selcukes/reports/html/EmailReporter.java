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

package io.github.selcukes.reports.html;

import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.commons.helper.FileHelper;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class EmailReporter implements IReporter {
    private static final String REPORT_TEMPLATE = "reportTemplate.html";
    private static final String ROW_TEMPLATE = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        String reportTemplate = initReportTemplate();

        final String body = suites
            .stream()
            .flatMap(suiteToResults())
            .collect(Collectors.joining());

        saveReportTemplate(outputDirectory, reportTemplate.replaceFirst("</tbody>", String.format("%s</tbody>", body)));
    }

    private Function<ISuite, Stream<? extends String>> suiteToResults() {
        return suite -> suite.getResults().entrySet()
            .stream()
            .flatMap(resultsToRows(suite));
    }

    private Function<Map.Entry<String, ISuiteResult>, Stream<? extends String>> resultsToRows(ISuite suite) {
        return e -> {
            ITestContext testContext = e.getValue().getTestContext();

            Set<ITestResult> failedTests = testContext
                .getFailedTests()
                .getAllResults();
            Set<ITestResult> passedTests = testContext
                .getPassedTests()
                .getAllResults();
            Set<ITestResult> skippedTests = testContext
                .getSkippedTests()
                .getAllResults();

            String suiteName = suite.getName();

            return Stream
                .of(failedTests, passedTests, skippedTests)
                .flatMap(results -> generateReportRows(e.getKey(), suiteName, results).stream());
        };
    }

    private List<String> generateReportRows(String testName, String suiteName, Set<ITestResult> allTestResults) {
        return allTestResults.stream()
            .map(testResultToResultRow(testName, suiteName))
            .collect(toList());
    }

    private Function<ITestResult, String> testResultToResultRow(String testName, String suiteName) {
        return testResult -> {
            switch (testResult.getStatus()) {
                case ITestResult.FAILURE:
                    return String.format(ROW_TEMPLATE, "danger", suiteName, testName, testResult.getName(), "FAILED", "NA");

                case ITestResult.SUCCESS:
                    return String.format(ROW_TEMPLATE, "success", suiteName, testName, testResult.getName(), "PASSED", (testResult.getEndMillis() - testResult.getStartMillis()));

                case ITestResult.SKIP:
                    return String.format(ROW_TEMPLATE, "warning", suiteName, testName, testResult.getName(), "SKIPPED", "NA");

                default:
                    return "";
            }
        };
    }

    private String initReportTemplate() {
        String template;
        byte[] reportTemplate;
        try {

            Path templatePath = Paths.get(Objects.requireNonNull(EmailReporter.class.getClassLoader()
                .getResource(REPORT_TEMPLATE)).toURI());
            reportTemplate = Files.readAllBytes(templatePath);
            template = new String(reportTemplate, StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            throw new SelcukesException("Failed initializing template..", e);
        }
        return template;
    }

    private void saveReportTemplate(String outputDirectory, String reportTemplate) {
        FileHelper.createDirectory(new File(outputDirectory));
        try {
            PrintWriter reportWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(outputDirectory, "log-report.html"))));
            reportWriter.println(reportTemplate);
            reportWriter.flush();
            reportWriter.close();
        } catch (IOException e) {
            throw new SelcukesException("Failed saving template..", e);
        }
    }
}