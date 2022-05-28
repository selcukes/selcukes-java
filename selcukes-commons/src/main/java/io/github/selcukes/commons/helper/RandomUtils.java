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

package io.github.selcukes.commons.helper;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class RandomUtils {
    private SecureRandom random() {
        return new SecureRandom();
    }

    public String randomChars(final int leftLimit, final int rightLimit, final int length) {
        return random().ints(leftLimit, rightLimit + 1).limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public String randomAlphaNumeric(final int length) {
        int leftLimit = 48;
        int rightLimit = 122;
        return random().ints(leftLimit, rightLimit + 1).limit(length)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public String randomAscii(final int length) {
        return randomChars(33, 47, length);
    }

    public String randomNumeric(final int length) {
        return randomChars(48, 57, length);
    }

    public String randomAlphabetic(final int length) {
        return randomChars(97, 122, length);
    }

}
