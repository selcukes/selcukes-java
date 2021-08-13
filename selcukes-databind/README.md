# Selcukes DataBind

[![Maven Central](https://img.shields.io/maven-central/v/io.github.selcukes/selcukes-databind.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.selcukes%22%20AND%20a:%22selcukes-databind%22)
[![Build Status](https://travis-ci.org/selcukes/selcukes-databind.svg?branch=master)](https://travis-ci.org/selcukes/selcukes-databind)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b75cd866e64b4111a5ca7a076b8cca68)](https://www.codacy.com/gh/selcukes/selcukes-databind?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=selcukes/selcukes-databind&amp;utm_campaign=Badge_Grade)
[![Vulnerability](https://sonarcloud.io/api/project_badges/measure?project=selcukes_selcukes-databind&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=selcukes_selcukes-databind)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=selcukes_selcukes-databind&metric=coverage)](https://sonarcloud.io/dashboard?id=selcukes_selcukes-databind)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=selcukes_selcukes-databind&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=selcukes_selcukes-databind)
[![badge-jdk](https://img.shields.io/badge/jdk-11-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License badge](https://img.shields.io/badge/license-Apache%202.0-blue.svg?label=License)](http://www.apache.org/licenses/LICENSE-2.0)

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
##Usage
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
