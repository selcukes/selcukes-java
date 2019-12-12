# DriverPool
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
<ul>
  DriverPool allow to download binary with specify details as follows
<li>custom download location - targetPath("downloadLocation")</li>
<li>specific architecture - arch64()/arch32()</li>
<li>Version Number- version("v0.26.0")</li>
</ul>

```java
DriverPool.firefoxDriver().arch64().targetPath("temp").version("v0.26.0").setup();
```
