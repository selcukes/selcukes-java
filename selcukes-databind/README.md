# Selcukes DataBind

selcukes-databind helps to parse Json and Yml files

To use add the `selcukes-databind` dependency to your pom.xml:

```xml
<dependencies>
  [...]
    <dependency>
        <groupId>io.github.selcukes</groupId>
        <artifactId>selcukes-databind</artifactId>
        <version>${selcukes-databind.version}</version>
    </dependency>
  [...]
</dependencies>

```

## Example 1

### Data file content

Test data file test-users.json

```json
{
  "users": [
    {
      "username": "junk",
      "password": "things"
    },
    {
      "username": "spam",
      "password": "eggs"
    }
  ]
}
```

### Usage

```java

public class DataMapperTest {
    
    //Pojo class for test data file test-users.json
    @Data
    @DataFile
    static class TestUsers {
        List<User> users;
    }

    @Data
    static class User {
        private String username;
        private String password;

    }

    @DataProvider
    public Iterator<Object[]> getTestUsers() {
        final TestUsers testUsers = DataMapper.parse(TestUsers.class);
        final List<Object[]> data = new ArrayList<>();
        testUsers.getUsers()
            .forEach(user -> data.add(new Object[]{user}));
        return data.iterator();
    }

    @Test(dataProvider = "getTestUsers")
    public void jsonTest(User user) {
        Assert.assertFalse(user.getUsername().isBlank());
        System.out.println("Username[" + user.getUsername() + "] Password[" + user.getPassword()+"]");
    }
}
```

## Example 2

Update test file runtime

Test data file test_sample.yml

```yaml
users:
  user1:
    username: "junk"
    password: "4177472e-23a3-4426-893f-8a794af7189c"
  user2:
    username: "spam"
    password: "40aafad2-1d24-4d6c-85e2-b7630dc17c57"
```

```java
public class DataMapperWriteTest {
    @SneakyThrows
    @Test
    public void testClass() {
        UUID uuid = UUID.randomUUID();
        TestSample testSample = DataMapper.parse(TestSample.class);
        testSample.getUsers().get("user1").put("password", uuid.toString());
        DataMapper.write(testSample);
    }

    @Data
    @DataFile(fileName = "test_sample.yml")
    static class TestSample {
        Map<String, Map<String, String>> users;
    }

}
```
