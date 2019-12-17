# Driver Pool
[![Maven Central](https://img.shields.io/maven-central/v/io.github.selcukes/driver-pool.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.selcukes%22%20AND%20a:%22driver-pool%22)
[![Build Status](https://travis-ci.org/selcukes/driver-pool.svg?branch=master)](https://travis-ci.org/selcukes/driver-pool)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/79fbd725ee664ff985fb66d4ae2a7527)](https://www.codacy.com/manual/selcukes/driver-pool?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=selcukes/driver-pool&amp;utm_campaign=Badge_Grade)
[![Vulnerability](https://sonarcloud.io/api/project_badges/measure?project=selcukes_driver-pool&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=selcukes_driver-pool)
[![badge-jdk](https://img.shields.io/badge/jdk-8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License badge](https://img.shields.io/badge/license-Apache2-green.svg?label=License)](http://www.apache.org/licenses/LICENSE-2.0)

Driver Pool automatically downloads and configures the binary drivers (e.g. chromedriver, geckodriver, etc.) required by Selenium WebDriver.

To use add the `driver-pool` dependency to your pom.xml:

```xml
<dependencies>
  [...]
    <dependency>
        <groupId>io.github.selcukes</groupId>
        <artifactId>driver-pool</artifactId>
        <version>${driver-pool.version}</version>
    </dependency>
  [...]
</dependencies>

```

## Usage
Download the latest binary
```java
DriverPool.chromedriver().setup();
DriverPool.firefoxDriver().setup();
DriverPool.iedriver().setup();
```
Download binaries for specific architecture

```java
DriverPool.chromedriver().arch64().setup();
DriverPool.firefoxDriver().arch32().setup();
```

Download binaries by specifying custom download location

```java
DriverPool.chromedriver().targetPath("temp").setup();
DriverPool.firefoxDriver().targetPath("downloadLocation").setup();
```

Download binaries for a specific release version
```java
DriverPool.firefoxDriver().version("v0.26.0").setup();
```
