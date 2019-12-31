# WebDriver Binaries
[![Maven Central](https://img.shields.io/maven-central/v/io.github.selcukes/webdriver-binaries.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.selcukes%22%20AND%20a:%22webdriver-binaries%22)
[![Build Status](https://travis-ci.org/selcukes/webdriver-binaries.svg?branch=master)](https://travis-ci.org/selcukes/webdriver-binaries)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/79fbd725ee664ff985fb66d4ae2a7527)](https://www.codacy.com/manual/selcukes/webdriver-binaries?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=selcukes/webdriver-binaries&amp;utm_campaign=Badge_Grade)
[![Vulnerability](https://sonarcloud.io/api/project_badges/measure?project=selcukes_webdriver-binaries&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=selcukes_webdriver-binaries)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=selcukes_webdriver-binaries&metric=coverage)](https://sonarcloud.io/dashboard?id=selcukes_webdriver-binaries)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=selcukes_webdriver-binaries&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=selcukes_webdriver-binaries)
[![badge-jdk](https://img.shields.io/badge/jdk-8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![Github Releases](https://img.shields.io/github/downloads/selcukes/webdriver-binaries/total.svg)](https://github.com/selcukes/webdriver-binaries/releases)
[![License badge](https://img.shields.io/badge/license-Apache%202.0-blue.svg?label=License)](http://www.apache.org/licenses/LICENSE-2.0)

WebDriver Binaries automatically downloads and configures the binary drivers (e.g. chromedriver, geckodriver, etc.) required by Selenium WebDriver.

To use add the `webdriver-binaries` dependency to your pom.xml:

```xml
<dependencies>
  [...]
    <dependency>
        <groupId>io.github.selcukes</groupId>
        <artifactId>webdriver-binaries</artifactId>
        <version>${webdriver-binaries.version}</version>
    </dependency>
  [...]
</dependencies>

```

## Usage
Download the latest binary
```java
WebDriverBinary.chromeDriver().setup();
WebDriverBinary.firefoxDriver().setup();
WebDriverBinary.ieDriver().setup();
WebDriverBinary.edgeDriver().setup();
WebDriverBinary.edgeDriver().setup();
WebDriverBinary.operaDriver().setup();
WebDriverBinary.grid().setup();	//Download Selenium Server Standalone jar
```
Download binaries for specific architecture

```java
WebDriverBinary.chromeDriver().arch64().setup();
WebDriverBinary.firefoxDriver().arch32().setup();
```

Download binaries by specifying custom download location

```java
WebDriverBinary.chromeDriver().targetPath("temp").setup();
WebDriverBinary.firefoxDriver().targetPath("downloadLocation").setup();
```

Download binaries for a specific release version
```java
WebDriverBinary.firefoxDriver().version("v0.26.0").setup();
```
## Examples
Check out <a href="https://github.com/selcukes/webdriver-binaries/wiki/Configure-WebDriverBinary-to-TestNG-Test">Wiki page</a> for sample TestNG Test
