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
### Encryptor
This helps to Encryption/Decryption user credential
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
- Shell Command Executor
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
### Properties:
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
- WebClient - Http Client Utils
### Common Helpers
- DateHelper
- ExceptionHelper
- ImageUtils
### Preconditions
This class provides a list of static methods for checking that a method or a constructor is invoked with valid parameter values. If a precondition fails, a tailored exception is thrown
```java
We can get a meaningful error message from the checkArgument method by passing an error message:

@Test
public void givenErrorMsg_whenCheckArgEvalsFalse_throwsException() {
    int age = -18;
    String message = "Age can't be zero or less than zero.";
 
    assertThatThrownBy(() -> Preconditions.checkArgument(age > 0, message))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage(message).hasNoCause();
}
```
- RandomUtils
