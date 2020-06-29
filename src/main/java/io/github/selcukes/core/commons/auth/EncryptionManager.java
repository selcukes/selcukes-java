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

import io.github.selcukes.core.helper.Preconditions;
import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

@UtilityClass
public class EncryptionManager {
    private final int DEFAULT_LENGTH = 128;
    private final String KEY_ALGORITHM = "AES";
    private final String ALGORITHM = "AES/GCM/NoPadding";

    public byte[] encryptData(String key, byte[] data) throws GeneralSecurityException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);
        SecretKey secretKey = generateSecretKey(key, iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(DEFAULT_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedData.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedData);
        return byteBuffer.array();
    }


    public byte[] decryptData(String key, byte[] encryptedData) throws GeneralSecurityException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        int dataSize = byteBuffer.getInt();
        Preconditions.checkArgument(!(dataSize < 12 || dataSize >= 16),
            "Data size is incorrect. Make sure that the incoming data is an AES encrypted file.");
        byte[] iv = new byte[dataSize];
        byteBuffer.get(iv);
        SecretKey secretKey = generateSecretKey(key, iv);
        byte[] cipherBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherBytes);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(DEFAULT_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        return cipher.doFinal(cipherBytes);

    }

    public SecretKey generateSecretKey(String password, byte[] iv) throws GeneralSecurityException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), iv, 65536, DEFAULT_LENGTH);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }
}

