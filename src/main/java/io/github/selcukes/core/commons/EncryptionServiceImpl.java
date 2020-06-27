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

package io.github.selcukes.core.commons;

import io.github.selcukes.core.exception.SelcukesException;
import io.github.selcukes.core.helper.Preconditions;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class EncryptionServiceImpl implements EncryptionService {

    @Override
    public String getPrefix() {
        return "℗:";
    }

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
            aesKey = buildKey16char(cryptoKey);
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(new byte[16]));
            return getPrefix() + Base64.encodeBase64String(cipher.doFinal(text.getBytes()));
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new SelcukesException(e);
        }

    }

    @Override
    public String decrypt(String cryptoKey, String encrypted) {
        Preconditions.checkArgument(encrypted.startsWith(getPrefix()), "Invalid Encrypted key");
        Key aesKey = null;
        if (cryptoKey != null && !"".equals(cryptoKey)) {
            aesKey = buildKey16char(cryptoKey);
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(new byte[16]));
            return new String(cipher.doFinal(Base64.decodeBase64(encrypted.substring(getPrefix().length(), encrypted.length()))));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new SelcukesException(e);
        }
    }

    private Key buildKey16char(String cryptoKey) {
        Key aesKey;
        StringBuilder cryptoKeyBuilder = new StringBuilder();
        cryptoKeyBuilder.append(cryptoKey);
        do {
            cryptoKeyBuilder.append(cryptoKey);
        } while (cryptoKeyBuilder.length() < 16);
        aesKey = new SecretKeySpec(cryptoKeyBuilder.toString().substring(0, 16).getBytes(), "AES");
        return aesKey;
    }

    private String getCryptoKey() {
        return System.getProperty("selcukes.crypto.key");
    }

}
