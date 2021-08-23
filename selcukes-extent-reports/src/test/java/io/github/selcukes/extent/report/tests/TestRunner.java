package io.github.selcukes.extent.report.tests;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = {
    "io.github.selcukes.extent.report.SelcukesExtentAdapter:",
    "html:target/cucumber-reports/cucumber.html", "json:target/cucumber-reports/cucumber.json"

})

public class TestRunner extends AbstractTestNGCucumberTests {


}
