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

package io.github.selcukes.databind.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.databind.exception.DataMapperException;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.github.selcukes.databind.properties.PropertiesMapper.systemProperties;

/**
 * This class contains a bunch of static methods that help you manipulate
 * strings.
 */
@UtilityClass
public class StringHelper {

    // A static final variable that is a predicate that takes a string and
    // returns a boolean value.
    public static final Predicate<String> nullOrEmpty = StringHelper::isNullOrEmpty;
    private static final String SNAKE_CASE_REGEX = "([a-z])([A-Z]+)";
    private static final String CAMEL_CASE_REGEX = "[^a-zA-Z0-9]";
    private static final String INTERPOLATE_REGEX = "\\$\\{(.+?)}";
    private static final String VERSION_NUMBER_REGEX = "[^0-9_.]";

    /**
     * `text == null || text.isEmpty() || text.isBlank()`
     * <p>
     * The above function is a boolean expression that returns true if the text
     * is null, empty, or blank
     *
     * @param  text The text to check.
     * @return      A boolean value
     */
    public boolean isNullOrEmpty(final String text) {
        return text == null || text.isEmpty() || text.isBlank();
    }

    /**
     * It takes a string, replaces all the camel case with underscores, and then
     * converts the string to lower case
     *
     * @param  text The text to convert to snake case.
     * @return      A string that is the text parameter with the first letter of
     *              each word capitalized.
     */
    public String toSnakeCase(final String text) {
        final String replacement = "$1_$2";
        return text.replaceAll(SNAKE_CASE_REGEX, replacement).toLowerCase();
    }

    /**
     * It takes a string, replaces all non-alphanumeric characters with
     * underscores, splits the string on underscores, capitalizes the first
     * letter of each word, and joins the words back together
     *
     * @param  text The text to convert to camel case.
     * @return      A string that is in camel case.
     */
    public String toCamelCase(final String text) {

        final StringBuilder stringBuilder = new StringBuilder(text.length());

        for (final String word : text.replaceAll(CAMEL_CASE_REGEX, "_").split("_")) {
            if (!word.isEmpty()) {
                stringBuilder.append(Character.toUpperCase(word.charAt(0)));
                stringBuilder.append(word.substring(1).toLowerCase());
            }
        }

        return stringBuilder.toString();
    }

    /**
     * It takes a string, removes all non-alphanumeric characters, and returns
     * the first character in lowercase
     *
     * @param  text The text to be converted.
     * @return      The first letter of the string is being returned in
     *              lowercase.
     */
    public static String toFieldName(final String text) {
        final String fieldName = text.replaceAll(CAMEL_CASE_REGEX, "");
        return fieldName.length() > 0 ? fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1) : null;
    }

    /**
     * It takes a string and a function that takes a match result and returns a
     * string, and returns a string
     *
     * @param  text     The text to interpolate.
     * @param  replacer A function that takes a MatchResult and returns a
     *                  String.
     * @return          A string with the interpolated values.
     */
    public String interpolate(final String text, final Function<MatchResult, String> replacer) {

        return Pattern.compile(INTERPOLATE_REGEX)
                .matcher(text)
                .replaceAll(replacer);
    }

    /**
     * It takes a string and returns a string with all the version numbers
     * removed
     *
     * @param  text The text to be processed.
     * @return      The version number is being returned.
     */
    public String extractVersionNumber(final String text) {
        return text.replaceAll(VERSION_NUMBER_REGEX, "");
    }

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

    /**
     * If the value is "date" or "datetime", then return the current date or
     * date/time in the specified format. Otherwise, return the value of the
     * system property with the specified name
     *
     * @param  value  The value to be substituted.
     * @param  format The format of the date/time.
     * @return        The value of the system property with the given name.
     */
    public String substitute(final String value, final String format) {
        if (value.equalsIgnoreCase("date")) {
            return Clocks.date(format);
        } else if (value.equalsIgnoreCase("datetime")) {
            return Clocks.dateTime(format);
        } else {
            return systemProperties().getProperty(value);
        }
    }

    /**
     * It replaces all non-breaking spaces, carriage returns, and line feeds
     * with a single space, and then trims the result
     *
     * @param  text The text to be normalized.
     * @return      The text is being returned with all the white spaces
     *              removed.
     */
    public static String normalizeText(final String text) {
        return text != null ? text.replaceAll("\u00A0|\\r\\n|\\r|\\n", " ").trim() : null;
    }

    /**
     * It splits the input string by newline, then splits each line by the delimiter, and finally returns a list of lists
     * of strings
     *
     * @param line The string to be split into a list of lists.
     * @param delimiter The delimiter to use when splitting the line.
     * @return A list of lists of strings.
     */
    public static List<List<String>> asListOfList(String line, String delimiter) {
        return Arrays.stream(line.split("\n"))
                .map(row -> Arrays.asList(row.split(delimiter)))
                .collect(Collectors.toList());
    }
}
