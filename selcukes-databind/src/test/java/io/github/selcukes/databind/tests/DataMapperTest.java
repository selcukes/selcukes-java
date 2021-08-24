/*
 *
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.selcukes.databind.tests;

import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.annotation.DataFile;
import lombok.Data;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataMapperTest {
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
        System.out.println("Username[" + user.getUsername() + "] Password[" + user.getPassword() + "]");
    }

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
}
