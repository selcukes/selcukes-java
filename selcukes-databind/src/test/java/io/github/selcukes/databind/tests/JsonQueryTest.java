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

import io.github.selcukes.databind.utils.JsonQuery;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class JsonQueryTest {

    private JsonQuery jsonQuery;

    @BeforeMethod
    public void setUp() {
        String json = """
                {
                   "store": {
                     "book": [
                       {
                         "category": "reference",
                         "author": "Nigel Rees",
                         "title": "Sayings of the Century",
                         "price": 8.95,
                         "details": {
                           "publisher": "Publisher A",
                           "year": 2000
                         }
                       },
                       {
                         "category": "fiction",
                         "author": "Evelyn Waugh",
                         "title": "Sword of Honour",
                         "price": 12.99,
                         "details": {
                           "publisher": "Publisher B",
                           "year": 2005
                         }
                       },
                       {
                         "category": "fiction",
                         "author": "Herman Melville",
                         "title": "Moby Dick",
                         "isbn": "0-553-21311-3",
                         "price": 8.99,
                         "details": {
                           "publisher": "Publisher C",
                           "year": 2006
                         }
                       },
                       {
                         "category": "fiction",
                         "author": "J. R. R. Tolkien",
                         "title": "The Lord of the Rings",
                         "isbn": "0-395-19395-8",
                         "price": 22.99,
                         "details": {
                           "publisher": "Publisher D",
                           "year": 2024
                         }
                       }
                     ],
                     "bicycle": {
                       "color": "red",
                       "price": 19.95
                     }
                   }
                 }
                """;
        jsonQuery = new JsonQuery(json); // Initialize the JsonQuery object
                                         // before each test
    }

    @Test
    public void shouldConstructJsonQueryInstanceFromJsonString() {
        Assert.assertNotNull(jsonQuery, "JsonQuery instance should be created from JSON string.");
    }

    @Test
    public void shouldGetStringValueForAuthorPath() {
        String author = jsonQuery.get("store.book[0].author", String.class);
        Assert.assertEquals(author, "Nigel Rees", "Expected author is 'Nigel Rees'.");
    }

    @Test
    public void shouldRetrieveMapValueForDetailsPath() {
        var actualBookDetails = jsonQuery.get("store.book[3].details", Map.class);
        Map<String, Object> expectedBookDetails = Map.of("publisher", "Publisher D", "year", 2024);
        Assert.assertEquals(actualBookDetails, expectedBookDetails,
            "The book details should match the expected values.");
    }

    @Test
    public void shouldReturnNullForMissingValue() {
        String missingValue = jsonQuery.get("store.book[0].publisher", String.class);
        Assert.assertNull(missingValue, "Missing value should return null.");
    }

    @Test
    public void shouldGetListOfBookTitles() {
        List<String> titles = jsonQuery.getList("store.book[*].title", String.class);
        Assert.assertEquals(titles.size(), 4, "There should be 4 book titles.");
        Assert.assertTrue(titles.contains("Moby Dick"), "The list of titles should contain 'Moby Dick'.");
        Assert.assertTrue(titles.contains("The Lord of the Rings"),
            "The list of titles should contain 'The Lord of the Rings'.");
    }

    @Test
    public void shouldRetrieveListOfDetailsForAllBooks() {
        var bookDetails = jsonQuery.getList("store.book[*].details", Map.class);
        Map<String, Object> expectedDetailsBook1 = Map.of("publisher", "Publisher A", "year", 2000);
        Map<String, Object> expectedDetailsBook2 = Map.of("publisher", "Publisher B", "year", 2005);
        Map<String, Object> expectedDetailsBook3 = Map.of("publisher", "Publisher C", "year", 2006);
        Map<String, Object> expectedDetailsBook4 = Map.of("publisher", "Publisher D", "year", 2024);
        Assert.assertEquals(bookDetails.size(), 4, "There should be 4 book details.");
        Assert.assertEquals(bookDetails.get(0), expectedDetailsBook1, "Details for the first book should match.");
        Assert.assertEquals(bookDetails.get(1), expectedDetailsBook2, "Details for the second book should match.");
        Assert.assertEquals(bookDetails.get(2), expectedDetailsBook3, "Details for the third book should match.");
        Assert.assertEquals(bookDetails.get(3), expectedDetailsBook4, "Details for the fourth book should match.");
    }

    @Test
    public void shouldRetrieveListFromNonArrayPath() {
        List<String> color = jsonQuery.getList("store.bicycle.color", String.class);
        Assert.assertEquals(color.size(), 1, "There should be exactly 1 color.");
        Assert.assertEquals(color.get(0), "red", "The color of the bicycle should be 'red'.");
    }

    @Test
    public void shouldRetrievePublishersFromDeeplyNestedStructure() {
        List<String> publishers = jsonQuery.getList("store.book[*].details.publisher", String.class);
        Assert.assertEquals(publishers.size(), 4, "There should be 4 publishers.");
        Assert.assertTrue(publishers.contains("Publisher A"), "Publishers should contain 'Publisher A'.");
        Assert.assertTrue(publishers.contains("Publisher B"), "Publishers should contain 'Publisher B'.");
    }

    @Test
    public void shouldReturnEmptyListForNonExistentArray() {
        String emptyJson = "{}";
        JsonQuery emptyJsonQuery = new JsonQuery(emptyJson);
        List<String> emptyList = emptyJsonQuery.getList("store.book[*].title", String.class);
        Assert.assertTrue(emptyList.isEmpty(), "The list should be empty for a non-existent array.");
    }
}
