# Selcukes Commons

selcukes-commons provides components needed to discover, parse files. 

To use add the `selcukes-commons` dependency to your pom.xml:

```xml
<dependencies>
  [...]
    <dependency>
        <groupId>io.github.selcukes</groupId>
        <artifactId>selcukes-commons</artifactId>
        <version>${selcukes-commons.version}</version>
    </dependency>
  [...]
</dependencies>

```
## Table of contents
  - [Encryptor](#Encryptor)  
  - [Logger](#Logger)
  - [Run Command](#Run-Command)
  - [Business Friendly Exceptions](#Business-Friendly-Exceptions)
  - [Properties](#Properties)
  - [WebClient](#WebClient)
  - [DateHelper](#DateHelper)
  - [Preconditions](#Preconditions)
  - [RandomUtils](#RandomUtils)
  
### Encryptor
This helps to Encryption/Decryption of user credentials
```java
public class EncryptionTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String plainText = "password";
    private Encryptor encryptor;
    private String encryptedText;

    @BeforeTest
    public void beforeTest() {
        encryptor = new StringEncryptor();
        System.setProperty("selcukes.crypto.key", "Hello");
    }

    @Test
    public void encryptionTest() {
        encryptedText = encryptor.encrypt(plainText);
        logger.info(() -> "Encrypted Password: " + encryptedText);
    }

    @Test(dependsOnMethods = {"encryptionTest"})
    public void decryptionTest() {
        logger.info(() -> "Decrypted Password: " + encryptor.decrypt(encryptedText));
        Assert.assertEquals(plainText, encryptor.decrypt(encryptedText));
    }
}
```
### Logger
A Logging utility helps to attach logs in html reports
```java
public class LogRecordTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private LogRecordListener logRecordListener;

    @BeforeTest
    public void setup() {
        logRecordListener = new LogRecordListener();
        LoggerFactory.addListener(logRecordListener);
    }

    @AfterTest
    public void tearDown() {
        LoggerFactory.removeListener(logRecordListener);
    }

    @Test(threadPoolSize = 3, invocationCount = 3, timeOut = 1000)
    public void testLogRecords() {
        long id = Thread.currentThread().getId();
        logger.info(() -> "Test Started..." + id);
        logger.warn(() -> "This is sample warning!");
        logger.info(() -> "Login Successful..." + id);
        logger.error(() -> "This is sample error message");
        logger.debug(() -> "This is sample debug statement");
        logger.info(() -> "Test Completed..." + id);

        String allLogs = logRecordListener.getLogRecords()
                .map(LogRecord::getMessage)
                .collect(Collectors.joining("\n  --> ", "\n--ALL Logs-- \n\n  --> ", "\n\n--End Of Logs--"));

        System.out.println("Thread with id: " + id + allLogs);

        String infoLogs = logRecordListener.getLogRecords(Level.INFO)
                .map(LogRecord::getMessage)
                .collect(Collectors.joining("\n  --> ", "\n--Info Logs-- \n\n  --> ", "\n\n--End Of Logs--"));

        System.out.println("Thread with id: " + id + infoLogs);

    }
}
```
### Run Command
Shell class helps to run commands
```java
public class RunCommandTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void runCommandTest() {
        
        Shell shell = new Shell();
        ExecResults execResults = shell.runCommand("google-chrome --version");

        String[] words = execResults.getOutput().get(2).split(" ");
        String browserVersion = words[words.length - 1];

        logger.info(() -> "Browser Version Number: " + browserVersion);
        
    }
}
```
### Business Friendly Exceptions
```java
public class BusinessExceptionTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void createException() {

        try {
            throw new TimeoutException("Test not found");
        } catch (Exception e) {
            throw new BusinessException("Element not Found", e);
        }
    }

    @Test
    public void testException() {
        try {
            createException();
        } catch (BusinessException e) {
            logger.error(e, e.logError());
        }
    }
}
```
### Properties
LinkedProperties is a subclass of Properties that returns keys in insertion order by keeping an internal insertion order set of keys updated on each
write operation (put, putAll, clear, remove).
Does not control the usage of {@link java.util.Hashtable#entrySet()} which allows write operations as well.
```java
public class PropertiesTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void loadPropertiesMapTest() {
        final Map<String, String> propertiesMap = ConfigFactory.loadPropertiesMap("selcukes-test.properties");
        Assert.assertEquals(propertiesMap.size(), 2);
        Assert.assertEquals(propertiesMap.get("test.env"), "QA");
        Assert.assertEquals(propertiesMap.get("test.browser"), "CHROME");
        propertiesMap.forEach((k, v) -> logger.info(() -> String.format("Key :[%s]   Value :[%s]", k, v)));


    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void linkedPropertiesTest() {
        LinkedProperties linkedProperties = new LinkedProperties();
        Assert.assertFalse(linkedProperties.contains("value1"));
        linkedProperties.put("key1", "value1");
        Assert.assertTrue(linkedProperties.contains("value1"));
        linkedProperties.put("key2", "value2");
        Assert.assertTrue(linkedProperties.containsKey("key2"));
        Assert.assertEquals(linkedProperties.entrySet().size(), 2);
        linkedProperties.clear();
        Assert.assertEquals(linkedProperties.entrySet().size(), 0);
        linkedProperties.elements();
    }
}
```
### WebClient
WebClient class is a Http Client Utils which helps to get or post request
```java
public class WebClientTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void postTest() throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\":\"Ramesh\",");
        json.append("\"notes\":\"hello\"");
        json.append("}");

        WebClient client = new WebClient("https://httpbin.org/post");
        Response response = client.post(json);
        String body = IOUtils.toString(response.getResponseStream(), StandardCharsets.UTF_8);
        logger.info(() -> body);
    }

    @Test
    public void requestTest() throws IOException {

        WebClient client = new WebClient("https://httpbin.org/get");
        Response response = client.sendRequest();
        String body = IOUtils.toString(response.getResponseStream(), StandardCharsets.UTF_8);
        logger.info(() -> body);
    }
}
```
### DateHelper
```java
public class DateHelperTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testDate() {
        DateHelper dh = DateHelper.get();
        logger.info(dh::dateTime);
        logger.info(dh::timeStamp);
        logger.info(() -> DateHelper.get().setDate("10/08/2021").date());
        LocalDate currentDate = dh.currentDate();
        logger.info(() -> dh.setDateFormat("MM-dd-YY").formatDate(currentDate));
    }
}
```
### Preconditions
This class provides a list of static methods for checking that a method or a constructor is invoked with valid parameter values. If a precondition fails, a tailored exception is thrown

We can get a meaningful error message from the checkArgument method by passing an error message

```java

public class PreconditionsTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void checkArgAndThrowsException() {
        int age = -18;
        String message = "Age can't be zero or less than zero.";
        Preconditions.checkArgument(age > 0, message);
    }

}
```
### RandomUtils
```java
public class RandomUtilsTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void randomTest() {
        logger.info(() -> RandomUtils.randomAscii(10));
        logger.info(() -> RandomUtils.randomNumeric(10));
        logger.info(() -> RandomUtils.randomAlphabetic(10));
        logger.info(() -> RandomUtils.randomAlphaNumeric(10));
    }
}
```
