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
## Usage
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
```java

public class DataMapperTest {
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
