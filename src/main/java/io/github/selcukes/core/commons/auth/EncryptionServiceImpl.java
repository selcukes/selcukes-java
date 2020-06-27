/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.commons.auth;

import io.github.selcukes.core.exception.SelcukesException;
import io.github.selcukes.core.helper.Preconditions;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class EncryptionServiceImpl implements EncryptionService {
    private static final int DEFAULT_LENGTH = 16;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final String PREFIX = "൫:";

    @Override
    public String encrypt(String text) {
        return encrypt(getCryptoKey(), text);
    }

    @Override
    public String decrypt(String text) {
        return decrypt(getCryptoKey(), text);
    }

    @Override
    public String encrypt(String cryptoKey, String text) {
        Key aesKey = null;
        if (cryptoKey != null && !"".equals(cryptoKey)) {
            aesKey = generateKey(cryptoKey);
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, getIvParameterSpec());
            return PREFIX + Base64.encodeBase64String(cipher.doFinal(text.getBytes()));
        } catch (Exception e) {
            throw new SelcukesException(e);
        }

    }

    @Override
    public String decrypt(String cryptoKey, String encrypted) {
        Preconditions.checkArgument(encrypted.startsWith(PREFIX), "Invalid Encrypted key");
        Key aesKey = null;
        if (cryptoKey != null && !"".equals(cryptoKey)) {
            aesKey = generateKey(cryptoKey);
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, getIvParameterSpec());
            String encryptedString = encrypted.substring(PREFIX.length());
            return new String(cipher.doFinal(Base64.decodeBase64(encryptedString)));
        } catch (Exception e) {
            throw new SelcukesException(e);
        }
    }

    private Key generateKey(String cryptoKey) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(cryptoKey);
        do {
            keyBuilder.append(cryptoKey);
        } while (keyBuilder.length() < DEFAULT_LENGTH);
        byte[] payload = keyBuilder.toString().substring(0, DEFAULT_LENGTH).getBytes();
        return new SecretKeySpec(payload, KEY_ALGORITHM);
    }

    private String getCryptoKey() {
        return System.getProperty("selcukes.crypto.key");
    }

    private IvParameterSpec getIvParameterSpec() {
        return new IvParameterSpec(new byte[DEFAULT_LENGTH]);
    }

}
