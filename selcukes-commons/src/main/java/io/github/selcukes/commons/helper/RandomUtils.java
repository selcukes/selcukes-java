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

/**
 * It generates random numbers.
 */
@UtilityClass
public class RandomUtils {
    /**
     * This function returns a new SecureRandom object.
     *
     * @return A new instance of SecureRandom.
     */
    private SecureRandom random() {
        return new SecureRandom();
    }

    /**
     * It generates a random string of a given length, using the given range of
     * characters
     *
     * @param  leftLimit  The character code of the smallest character to be
     *                    generated.
     * @param  rightLimit the maximum value of the random number
     * @param  length     The length of the string you want to generate.
     * @return            A random string of characters.
     */
    public String randomChars(final int leftLimit, final int rightLimit, final int length) {
        return random().ints(leftLimit, rightLimit + 1).limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    /**
     * It generates a random string of length `length` that contains only
     * alphanumeric characters
     *
     * @param  length The length of the generated string.
     * @return        A random string of alphanumeric characters.
     */
    public String randomAlphaNumeric(final int length) {
        int leftLimit = 48;
        int rightLimit = 122;
        return random().ints(leftLimit, rightLimit + 1).limit(length)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    /**
     * > Generate a random string of ASCII characters
     *
     * @param  length the length of the string
     * @return        A random string of ASCII characters.
     */
    public String randomAscii(final int length) {
        return randomChars(33, 47, length);
    }

    /**
     * > Generate a random string of numbers of a given length
     *
     * @param  length The length of the string to be generated.
     * @return        A random string of numbers
     */
    public String randomNumeric(final int length) {
        return randomChars(48, 57, length);
    }

    /**
     * It returns a random string of alphabetic characters of the specified
     * length
     *
     * @param  length The length of the string to be generated.
     * @return        A random string of alphabetic characters.
     */
    public String randomAlphabetic(final int length) {
        return randomChars(97, 122, length);
    }

}
