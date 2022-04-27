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

import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@UtilityClass
public class StringHelper {
    public static final Predicate<String> nullOrEmpty = StringHelper::isNullOrEmpty;

    public boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty() || text.isBlank();
    }

    public String toSnakeCase(String text) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return text.replaceAll(regex, replacement).toLowerCase();
    }

    public String toCamelCase(String text) {
        String regex = "[^a-zA-Z0-9]";
        final StringBuilder stringBuilder = new StringBuilder(text.length());

        for (final String word : text.replaceAll(regex, "_").split("_")) {
            if (!word.isEmpty()) {
                stringBuilder.append(Character.toUpperCase(word.charAt(0)));
                stringBuilder.append(word.substring(1).toLowerCase());
            }
        }

        return stringBuilder.toString();
    }

    public String interpolate(String text, Function<MatchResult, String> replacer) {
        String regex = "\\$\\{(.+?)\\}";
        return Pattern.compile(regex)
            .matcher(text)
            .replaceAll(replacer);
    }

    public String extractVersionNumber(String text) {
        String regex = "[^0-9_.]";
        return text.replaceAll(regex, "");
    }
}
