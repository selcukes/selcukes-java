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
import org.testng.annotations.Test;

import java.util.List;

public class CreateDataFileTest {

    @Test
    public void createDataFileTest() {
        Details details1 = new Details();
        details1.setNumber("512");
        details1.setType("CSM");

        Details details2 = new Details();
        details2.setNumber("123");
        details2.setType("A-CSM");

        Customer customer = new Customer();
        customer.setAge(5);
        customer.setFirstName("Mark");
        customer.setLastName("Jones");
        customer.setContactDetails(List.of(details1, details2));

        DataMapper.write(customer);

        Customer newCustomer = DataMapper.parse(Customer.class);
        newCustomer.getContactDetails().forEach(details -> System.out.println(details.getType() + " : " + details.getNumber()));
    }

    @Data
    @DataFile(fileName = "customer.yml")
    static class Customer {
        String firstName;
        String lastName;
        int age;
        List<Details> contactDetails;
    }

    @Data
    static class Details {
        String type;
        String number;
    }

}
