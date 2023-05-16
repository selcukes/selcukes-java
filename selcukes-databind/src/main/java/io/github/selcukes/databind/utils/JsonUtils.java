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

package io.github.selcukes.databind.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.databind.exception.DataMapperException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {

    /**
     * Convert a POJO to a JSON string.
     *
     * @param  object The object to be converted to JSON
     * @return        A JSON string
     */
    public String toJson(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing JSON string from POJO[%s]" + object.getClass().getName(), e);
        }
    }

    /**
     * It takes an object and returns a pretty JSON string
     *
     * @param  object The object to be converted to JSON
     * @return        A JSON string
     */
    public String toPrettyJson(final Object object) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing JSON string from POJO[%s]" + object.getClass().getName(), e);
        }
    }

    /**
     * It takes a string and returns a JsonNode
     *
     * @param  content The string to be parsed into a JsonNode
     * @return         A JsonNode object
     */
    public JsonNode toJson(final String content) {
        try {
            var mapper = new ObjectMapper();
            var factory = mapper.getFactory();
            var parser = factory.createParser(content);
            return mapper.readTree(parser);
        } catch (Exception e) {
            throw new DataMapperException("Failed parsing string to JsonNode:\n" + content, e);
        }
    }
}
