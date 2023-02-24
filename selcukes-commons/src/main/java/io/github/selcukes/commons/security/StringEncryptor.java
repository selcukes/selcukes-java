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

import io.github.selcukes.commons.exception.EncryptionException;

import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Objects;

import static io.github.selcukes.commons.properties.SelcukesTestProperties.CRYPTO_KEY;

/**
 * It uses the Java Cryptography Architecture (JCA) to encrypt and decrypt a
 * String using a key
 */
public class StringEncryptor implements Encryptor {

    @Override
    public String encrypt(final String text) {
        return encrypt(getCryptoKey(), text);
    }

    @Override
    public String decrypt(final String text) {
        return decrypt(getCryptoKey(), text);
    }

    @Override
    public String encrypt(final String cryptoKey, final String text) {
        Objects.requireNonNull(text, "Password Text must not be null");
        Objects.requireNonNull(cryptoKey, "Crypto Key must not be null");
        try {
            byte[] textToEncrypt = ByteEncryptor.encryptData(cryptoKey, text.getBytes());
            return Base64.getEncoder().encodeToString(textToEncrypt);
        } catch (GeneralSecurityException e) {
            throw new EncryptionException(String.format("Failed Encrypting text[%s]: ", text), e);
        }
    }

    @Override
    public String decrypt(final String cryptoKey, final String encrypted) {
        Objects.requireNonNull(encrypted, "Encrypted Text must not be null");
        Objects.requireNonNull(cryptoKey, "Crypto Key must not be null");
        try {
            byte[] textToDecrypt = Base64.getDecoder().decode(encrypted);
            return new String(ByteEncryptor.decryptData(cryptoKey, textToDecrypt));
        } catch (Exception e) {
            throw new EncryptionException(String.format("Failed Decrypting text[%s] : ", encrypted), e);
        }
    }

    private String getCryptoKey() {
        return System.getProperty(CRYPTO_KEY);
    }

}
