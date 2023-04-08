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

package io.github.selcukes.databind.tests.collect;

import io.github.selcukes.databind.collections.Lists;
import io.github.selcukes.databind.collections.Streams;
import io.github.selcukes.databind.utils.Clocks;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CollectionsTest {

    @Test
    public void listIgnoreCaseTest() {
        var list = Lists.ofIgnoreCase("World");
        assertTrue(list.contains("world"));
    }

    @Test
    public void mapOfListTest() {
        var listMap = List.of(
            Map.of("a", 1, "b", 2),
            Map.of("a", 4, "b", 5),
            Map.of("a", 6, "b", 7));
        var mapOfList = Streams.toMapOfList(listMap);
        assertEquals(mapOfList.get("a").get(0), 1);
    }

    @Test
    public void toTableTest() {
        List<List<Object>> listOfList = List.of(
            List.of("a", "b", "c"),
            List.of(Clocks.dateNow(), Clocks.dateNow().plusDays(10), Clocks.dateNow().withYear(2033)),
            List.of(4, 5, 6));
        var table = Streams.toTable(listOfList);

        assertEquals(table.get(0).get("a"), Clocks.date("yyyy-MM-dd"));
    }

    @Test
    public void testFindFirstAndIndex() {
        List<String> strings = List.of("foo", "bar", "baz");

        Predicate<String> stringPredicate = s -> s.length() > 2;
        verifyFindFirstAndIndex(strings, stringPredicate, 0, "foo");

        List<Integer> integers = List.of(1, 2, 3, 4, 5);

        Predicate<Integer> integerPredicate = i -> i % 2 == 0;
        verifyFindFirstAndIndex(integers, integerPredicate, 1, 2);

        Predicate<Integer> integerPredicate1 = i -> i == 4;
        verifyFindFirstAndIndex(integers, integerPredicate1, 3, 4);
    }

    private <T> void verifyFindFirstAndIndex(List<T> list, Predicate<T> predicate, int expectedIndex, T expectedValue) {
        var findFirstValue = Streams.findFirst(list, predicate);
        var indexValue = Streams.indexOf(list, predicate);
        assertTrue(findFirstValue.isPresent());
        assertEquals(findFirstValue.get(), expectedValue);
        assertTrue(indexValue.isPresent());
        assertEquals(indexValue.getAsInt(), expectedIndex);
    }

    @Test
    public void testFindFirstAndIndex1() {

        var people = List.of(
            new Employee("John", "Developer", 1001),
            new Person("Alice", "Manager"),
            new Employee("Bob", "Engineer", 1002));
        Predicate<Person> predicate = person -> person.getName().equalsIgnoreCase("Alice");
        var index = Streams.indexOf(people, predicate);
        var firstPerson = Streams.findFirst(people, predicate);

        assertTrue(index.isPresent());
        assertEquals(index.getAsInt(), 1);
        assertTrue(firstPerson.isPresent());
        assertEquals(firstPerson.get().getJob(), "Manager");

    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Person {
        private String name;
        private String job;
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    private static class Employee extends Person {
        private int id;

        public Employee(final String name, final String job, final int id) {
            super(name, job);
            this.id = id;
        }
    }
}
