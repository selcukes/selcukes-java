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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class XmlTest {
    @Test
    public void xmlTest() {
        var customerInfo = DataMapper.parse(CustomerInfo.class);
        System.out.println(customerInfo.getMetaData().get("eventNotificationDate"));
        System.out.println(customerInfo.getContactPersonList().get(0).get("firstName"));
    }

    @Test
    public void updateXmlTest() {
        var excelData = Map.of(
            "contactPersonList", List.of(Map.of(
                "contactPersonId", "4567",
                "email", "test@test.com",
                "firstName", "Ramesh",
                "lastName", "Babu",
                "mobilePhone", "+15168978352",
                "title", "Treasury",
                "workPhone", "+19087253123")));
        var customerInfo = DataMapper.parse(CustomerInfo.class);
        var contactPersonList = customerInfo.getContactPersonList();
        /*
         * Streams.of(1,
         * contactPersonList.size()).forEach(contactPersonList::remove);
         * contactPersonList.get(0).clear();
         */
        contactPersonList.addAll(excelData.get("contactPersonList"));

        DataMapper.write(customerInfo);
        var customerInfo1 = DataMapper.parse(CustomerInfo.class);
        Assert.assertEquals(contactPersonList, customerInfo1.getContactPersonList());

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
