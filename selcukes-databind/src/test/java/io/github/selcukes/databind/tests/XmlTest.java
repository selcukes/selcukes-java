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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.annotation.DataFile;
import lombok.Data;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class XmlTest {
    @Test
    public void xmlTest() {
        CustomerInfo memberInfo = DataMapper.parse(CustomerInfo.class);
        System.out.println(memberInfo.getMetaData().get("eventNotificationDate"));
        System.out.println(memberInfo.getContactPersonList().get(0).get("firstName"));
    }

    @Data
    @DataFile(fileName = "CustomerInfo.xml")
    static class CustomerInfo {
        Map<String, String> metaData;
        String name;
        String maturityDate;
        Map<String, String> address;
        @JacksonXmlProperty(localName = "contactPerson")
        @JacksonXmlElementWrapper(localName = "contactPersonList")
        List<Map<String, String>> contactPersonList;
    }
}
