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
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for navigating and extracting data from JSON documents using a
 * path syntax.
 */
public class JsonQuery {

    private final JsonNode rootNode;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a JsonQuery instance from a JSON string.
     *
     * @param json the JSON string
     */
    @SneakyThrows
    public JsonQuery(String json) {
        this.rootNode = objectMapper.readTree(json);
    }

    /**
     * Constructs a JsonQuery instance from a JsonNode.
     *
     * @param node the JsonNode to use as the root node
     */
    public JsonQuery(JsonNode node) {
        this.rootNode = node;
    }

    /**
     * Factory method to create a JsonQuery instance from a JSON string.
     *
     * @param  json the JSON string
     * @return      a JsonQuery instance
     */
    public static JsonQuery from(String json) {
        return new JsonQuery(json);
    }

    /**
     * Retrieves a single value from the JSON document at the specified path.
     *
     * @param  path the path to the value
     * @param  type the class type of the value to retrieve
     * @param  <T>  the type of the value
     * @return      the value at the specified path, or null if not found
     */
    @SneakyThrows
    public <T> T get(String path, Class<T> type) {
        JsonNode node = getNode(path);
        return (node == null || node.isMissingNode()) ? null : objectMapper.treeToValue(node, type);
    }

    /**
     * Retrieves a list of values from the JSON document at the specified
     * wildcard path.
     *
     * @param  path the wildcard path to retrieve values from
     * @return      a list of values found at the specified path
     */
    public List<String> getList(String path) {
        List<String> values = new ArrayList<>();
        JsonNode currentNode = rootNode;

        String[] keys = path.split("\\.");
        for (String key : keys) {
            if (containsWildcard(key)) {
                return extractValuesFromArray(currentNode, keys, key, values);
            } else {
                currentNode = navigateToNode(currentNode, key);
                if (currentNode.isMissingNode()) {
                    return values; // Return empty if missing node
                }
            }
        }

        return extractFinalValues(currentNode, values);
    }

    /**
     * Extracts values from arrays using wildcard paths.
     *
     * @param  currentNode the current JSON node
     * @param  keys        the path keys
     * @param  key         the current key being processed
     * @param  values      the list to store extracted values
     * @return             a list of extracted values
     */
    private List<String> extractValuesFromArray(JsonNode currentNode, String[] keys, String key, List<String> values) {
        String arrayKey = extractArrayKey(key);
        var resultNode = currentNode.path(arrayKey);

        if (resultNode.isArray()) {
            for (JsonNode node : resultNode) {
                processArrayElement(keys, arrayKey, node, values);
            }
        }
        return values;
    }

    /**
     * Processes each element in the array and extracts the required values.
     *
     * @param keys     the path keys
     * @param arrayKey the current array key
     * @param node     the current JSON node being processed
     * @param values   the list to store extracted values
     */
    private void processArrayElement(String[] keys, String arrayKey, JsonNode node, List<String> values) {
        if (isLastKey(keys, arrayKey)) {
            values.add(node.asText());
        } else {
            JsonNode resultNode = getNodeFromSubPath(node, removeProcessedArrayKey(keys, arrayKey));
            if (resultNode != null && resultNode.isValueNode()) {
                values.add(resultNode.asText());
            }
        }
    }

    /**
     * Checks if the specified key contains a wildcard.
     *
     * @param  key the key to check
     * @return     true if the key contains a wildcard, false otherwise
     */
    private boolean containsWildcard(String key) {
        return key.contains("[*]");
    }

    /**
     * Extracts the key before the wildcard in a given key.
     *
     * @param  key the key containing a wildcard
     * @return     the extracted key before the wildcard
     */
    private String extractArrayKey(String key) {
        return key.substring(0, key.indexOf("[*]"));
    }

    /**
     * Removes the processed array key part from the path.
     *
     * @param  keys     the path keys
     * @param  arrayKey the current array key
     * @return          the remaining path as a string
     */
    private String removeProcessedArrayKey(String[] keys, String arrayKey) {
        var joinedKeys = String.join(".", keys);
        var pattern = arrayKey + "[*].";
        int index = joinedKeys.indexOf(pattern);
        return (index != -1) ? joinedKeys.substring(index + pattern.length()) : joinedKeys;
    }

    /**
     * Checks if the specified key is the last key in the path.
     *
     * @param  keys the path keys
     * @param  key  the key to check
     * @return      true if it is the last key, false otherwise
     */
    private boolean isLastKey(String[] keys, String key) {
        return keys[keys.length - 1].equals(key);
    }

    /**
     * Extracts values from the final node, which may be an array or a single
     * value.
     *
     * @param  currentNode the current JSON node
     * @param  values      the list to store extracted values
     * @return             a list of extracted values
     */
    private List<String> extractFinalValues(JsonNode currentNode, List<String> values) {
        if (currentNode.isArray()) {
            for (JsonNode node : currentNode) {
                values.add(node.asText());
            }
        } else if (currentNode.isValueNode()) {
            values.add(currentNode.asText());
        }
        return values;
    }

    /**
     * Retrieves a node from a sub-path within a given JSON node.
     *
     * @param  node    the JSON node to search within
     * @param  subPath the sub-path to search for
     * @return         the resulting JsonNode or null if not found
     */
    private JsonNode getNodeFromSubPath(JsonNode node, String subPath) {
        String[] keys = subPath.split("\\.");
        JsonNode currentNode = node;

        for (String key : keys) {
            currentNode = navigateToNode(currentNode, key);
            if (currentNode == null || currentNode.isMissingNode()) {
                return null;
            }
        }
        return currentNode;
    }

    /**
     * Navigates to the correct node by parsing the path.
     *
     * @param  currentNode the current JSON node
     * @param  key         the key to navigate
     * @return             the resulting JsonNode
     */
    private JsonNode navigateToNode(JsonNode currentNode, String key) {
        if (isArrayKey(key)) {
            return Objects.requireNonNull(getArrayNode(currentNode, key));
        }
        return currentNode.path(key);
    }

    /**
     * Checks if the key indicates an array access.
     *
     * @param  key the key to check
     * @return     true if the key indicates an array, false otherwise
     */
    private boolean isArrayKey(String key) {
        return key.contains("[") && key.contains("]");
    }

    /**
     * Retrieves a JsonNode for an array based on the specified key and index.
     *
     * @param  currentNode the current JSON node
     * @param  key         the array key with an index
     * @return             the resulting JsonNode or null if not found
     */
    private JsonNode getArrayNode(JsonNode currentNode, String key) {
        String arrayKey = key.substring(0, key.indexOf("["));
        int index = extractArrayIndex(key);
        currentNode = currentNode.path(arrayKey);

        return (currentNode.isArray()) ? currentNode.get(index) : null;
    }

    /**
     * Extracts the index value from an array key.
     *
     * @param  key                   the array key containing an index
     * @return                       the extracted index
     * @throws NumberFormatException if the index is not a valid integer
     */
    private int extractArrayIndex(String key) {
        return Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
    }

    /**
     * Retrieves a node based on a specified path.
     *
     * @param  path the path to retrieve
     * @return      the resulting JsonNode or null if not found
     */
    private JsonNode getNode(String path) {
        String[] keys = path.split("\\.");
        JsonNode currentNode = rootNode;

        for (String key : keys) {
            currentNode = navigateToNode(currentNode, key);
            if (currentNode == null || currentNode.isMissingNode()) {
                return null;
            }
        }
        return currentNode;
    }

}
