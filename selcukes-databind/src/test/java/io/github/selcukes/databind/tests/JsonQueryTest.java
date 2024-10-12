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
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class JsonQueryTest {
    private final String json = """
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

    @Test
    public void testJsonQueryConstructionFromString() {
        JsonQuery jsonQuery = new JsonQuery(json);
        Assert.assertNotNull(jsonQuery, "JsonQuery instance should be created from JSON string.");
    }

    @Test
    public void testGetStringValueByPath() {
        JsonQuery jsonQuery = new JsonQuery(json);
        String author = jsonQuery.get("store.book[0].author", String.class);
        Assert.assertEquals(author, "Nigel Rees", "Author should be Nigel Rees.");
    }

    @Test
    public void testGetMapValueByPath() {
        JsonQuery jsonParser = new JsonQuery(json);
        var actualBookDetails = jsonParser.get("store.book[3].details", Map.class);
        Map<String, Object> expectedBookDetails = Map.of("publisher", "Publisher D",
            "year", 2024);
        Assert.assertEquals(actualBookDetails, expectedBookDetails, "Book details do not match the expected values.");
    }

    @Test
    public void testGetMissingValue() {
        JsonQuery jsonQuery = new JsonQuery(json);
        String missingValue = jsonQuery.get("store.book[0].publisher", String.class);
        Assert.assertNull(missingValue, "Missing value should return null.");
    }

    @Test
    public void testGetListByPath() {
        JsonQuery jsonQuery = new JsonQuery(json);
        List<String> titles = jsonQuery.getList("store.book[*].title");
        Assert.assertEquals(titles.size(), 4, "Should return 4 book titles.");
        Assert.assertTrue(titles.contains("Moby Dick"), "Titles should contain 'Moby Dick'.");
        Assert.assertTrue(titles.contains("The Lord of the Rings"), "Titles should contain 'The Lord of the Rings'.");
    }

    @Test
    public void testGetListFromNonArrayPath() {
        JsonQuery jsonQuery = new JsonQuery(json);
        List<String> color = jsonQuery.getList("store.bicycle.color");
        Assert.assertEquals(color.size(), 1, "Should return 1 color.");
        Assert.assertEquals(color.get(0), "red", "Color should be 'red'.");
    }

    @Test
    public void testGetListFromDeeplyNestedStructure() {
        JsonQuery jsonQuery = new JsonQuery(json);
        List<String> publishers = jsonQuery.getList("store.book[*].details.publisher");
        Assert.assertEquals(publishers.size(), 4, "Should return 2 publishers.");
        Assert.assertTrue(publishers.contains("Publisher A"), "Should contain Publisher A.");
        Assert.assertTrue(publishers.contains("Publisher B"), "Should contain Publisher B.");
    }

    @Test
    public void testGetListFromEmptyArray() {
        String emptyJson = "{}"; // Empty JSON
        JsonQuery jsonQuery = new JsonQuery(emptyJson);
        List<String> emptyList = jsonQuery.getList("store.book[*].title");
        Assert.assertTrue(emptyList.isEmpty(), "List should be empty for non-existent array.");
    }
}
