# Driver Pool
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
