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

import io.github.selcukes.commons.helper.Preconditions;
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

/**
 * It's a class that encrypts and decrypts byte arrays
 */
@UtilityClass
public class ByteEncryptor {
    private final int DEFAULT_LENGTH = 128; // Due to the US export restriction JDK only ships 128bit version.
    private final int DEFAULT_IV_LENGTH = 12;
    private final String KEY_ALGORITHM = "AES";
    private final String DEFAULT_ALGORITHM = "AES/GCM/NoPadding";
    private final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private final int ITERATION_COUNT = 65536;
    private final int ITERATIONS = 4;

    public byte[] encryptData(final String key, final byte[] data) throws GeneralSecurityException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = newIV(DEFAULT_IV_LENGTH);
        secureRandom.nextBytes(iv);

        SecretKey secretKey = generateSecretKey(key, iv);

        byte[] cipherBytes = doCipher(Cipher.ENCRYPT_MODE, data, secretKey, iv);
        ByteBuffer byteBuffer = ByteBuffer.allocate(ITERATIONS + iv.length + cipherBytes.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherBytes);
        return byteBuffer.array();
    }

    public byte[] decryptData(final String key, final byte[] encryptedData) throws GeneralSecurityException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        int dataSize = byteBuffer.getInt();
        Preconditions.checkArgument(!(dataSize < DEFAULT_IV_LENGTH || dataSize >= DEFAULT_IV_LENGTH + ITERATIONS),
                "Data size is incorrect. Make sure that the incoming data is an AES encrypted file.");
        byte[] iv = newIV(dataSize);
        byteBuffer.get(iv);
        SecretKey secretKey = generateSecretKey(key, iv);
        byte[] cipherBytes = newIV(byteBuffer.remaining());
        byteBuffer.get(cipherBytes);
        return doCipher(Cipher.DECRYPT_MODE, cipherBytes, secretKey, iv);

    }

    public SecretKey generateSecretKey(final String password, final byte[] iv) throws GeneralSecurityException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), iv, ITERATION_COUNT, DEFAULT_LENGTH);
        byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    private byte[] doCipher(final int encryptionMode, final byte[] data, final SecretKey secretKey, final byte[] iv)
            throws GeneralSecurityException {

        Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM);
        cipher.init(encryptionMode, secretKey, new GCMParameterSpec(DEFAULT_LENGTH, iv));
        return cipher.doFinal(data);
    }

    private byte[] newIV(final int length) {
        return new byte[length];
    }

}

