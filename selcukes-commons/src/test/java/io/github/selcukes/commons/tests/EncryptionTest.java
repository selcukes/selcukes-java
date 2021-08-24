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

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.security.Encryptor;
import io.github.selcukes.commons.security.StringEncryptor;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EncryptionTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String plainText = "password";
    private Encryptor encryptor;
    private String encryptedText;

    @BeforeTest
    public void beforeTest() {
        encryptor = new StringEncryptor();
        System.setProperty("selcukes.crypto.key", "Hello");
    }

    @Test
    public void encryptionTest() {
        encryptedText = encryptor.encrypt(plainText);
        logger.info(() -> "Encrypted Password: " + encryptedText);
    }

    @Test(dependsOnMethods = {"encryptionTest"})
    public void decryptionTest() {
        logger.info(() -> "Decrypted Password: " + encryptor.decrypt(encryptedText));
        Assert.assertEquals(plainText, encryptor.decrypt(encryptedText));
    }


}
