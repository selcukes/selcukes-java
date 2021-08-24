# WebDriver Binaries

WebDriver Binaries automatically downloads and configures the binary drivers (e.g. chromedriver, geckodriver, etc.)
required by Selenium WebDriver.

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

Check out <a href="https://github.com/selcukes/webdriver-binaries/wiki/Configure-WebDriverBinary-to-TestNG-Test">Wiki
page</a> for sample TestNG Test
