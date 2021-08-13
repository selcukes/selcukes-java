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
import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

public class DataMapperWriteTest {
    @Data
    @DataFile
    static class TestUsers {
        List<DataMapperTest.User> users;
    }

    @Data
    static class User {
        private String username;
        private String password;

    }

    @SneakyThrows
    @Test
    public void testClass() {
        UUID uuid = UUID.randomUUID();
        TestUsers testUsers = DataMapper.parse(TestUsers.class);
        testUsers.getUsers().get(1).setPassword(uuid.toString());
        DataMapper.write(testUsers);
    }

}
