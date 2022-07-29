/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.security;

public interface Encryptor {
    /**
     * Encrypts the given text using the given key.
     *
     * @param text The text to be encrypted.
     * @return A string
     */
    String encrypt(String text);

    /**
     * Decrypts the given text using the given key.
     *
     * @param text The text to be decrypted.
     * @return The decrypted text.
     */
    String decrypt(String text);

    /**
     * "Encrypts the given text using the given crypto key."
     *
     * The function takes two parameters:
     *
     * * `cryptoKey`: The key to use for encryption.
     * * `text`: The text to encrypt
     *
     * @param cryptoKey The key used to encrypt the text.
     * @param text The text to be encrypted.
     * @return The encrypted text.
     */
    String encrypt(String cryptoKey, String text);

    /**
     * "Decrypts the given encrypted string using the given crypto key."
     *
     * The crypto key is a string that is used to encrypt and decrypt the data. It should be kept secret
     *
     * @param cryptoKey The key used to encrypt the data.
     * @param encrypted The encrypted string to decrypt.
     * @return The decrypted string.
     */
    String decrypt(String cryptoKey, String encrypted);
}
