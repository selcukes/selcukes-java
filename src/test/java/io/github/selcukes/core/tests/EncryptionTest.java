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

package io.github.selcukes.core.tests;

import io.github.selcukes.core.commons.auth.EncryptionService;
import io.github.selcukes.core.commons.auth.EncryptionServiceImpl;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EncryptionTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private EncryptionService encryptionService;
    private final String plainText = "password";
    private final String encryptedText = "℗:+xn6Yxw4a/zekUJYb3GYEg==";

    @BeforeTest
    public void beforeTest() {
        encryptionService = new EncryptionServiceImpl();
        System.setProperty("selcukes.crypto.key", "Hello");
    }

    @Test
    public void encryptionTest() {
        logger.info(() -> "Encrypted Password: " + encryptionService.encrypt(plainText));
        Assert.assertEquals(encryptedText, encryptionService.encrypt(plainText));
    }

    @Test
    public void decryptionTest() {
        logger.info(() -> "Decrypted Password: " + encryptionService.decrypt(encryptedText));
        Assert.assertEquals(plainText, encryptionService.decrypt(encryptedText));
    }


}
