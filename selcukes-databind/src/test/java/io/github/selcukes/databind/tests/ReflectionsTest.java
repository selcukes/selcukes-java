/*
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
 */

package io.github.selcukes.databind.tests;

import io.github.selcukes.collections.Reflections;
import io.github.selcukes.databind.DataMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ReflectionsTest {
    @Test
    public void reflectionsTest() {
        var memberInfo = DataMapper.parse(XmlTest.CustomerInfo.class);
        var persons = List.of(Map.of(
            "contactPersonId", "1256",
            "email", "test@test.com",
            "firstName", "Ramesh",
            "lastName", "Babu",
            "mobilePhone", "+15168978352",
            "title", "Sr QA",
            "workPhone", "+19087253123"),
            Map.of(
                "contactPersonId", "1266",
                "email", "test2@test.com",
                "firstName", "Ramesh1",
                "lastName", "Babu1",
                "mobilePhone", "+15168978000",
                "title", "QA",
                "workPhone", "+19087253000"));
        Reflections.setFieldValue(memberInfo, "contactPersonList", persons);
        Assert.assertEquals(memberInfo.getContactPersonList(), persons);
    }

}
