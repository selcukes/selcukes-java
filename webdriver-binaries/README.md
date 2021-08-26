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
## Usage
```java
public class WebDriverTest {

    private WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        WebDriverBinary.chromeDriver().setup();
    }

    @Before
    public void setupTest() {
        driver = new ChromeDriver();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void test() {
        // Your test code here
    }

}
```
