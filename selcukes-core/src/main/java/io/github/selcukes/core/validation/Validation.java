/*
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
 */

package io.github.selcukes.core.validation;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import static io.github.selcukes.commons.fixture.SelcukesFixture.fail;

@UtilityClass
@CustomLog
public class Validation {
    private static final ThreadLocal<List<String>> FAILED_MESSAGES = InheritableThreadLocal.withInitial(ArrayList::new);

    public static void failWithMessage(boolean isSoft, String errorMessage, Object... args) {
        String message = String.format(errorMessage, args);
        logger.info(() -> message);
        if (!isSoft) {
            fail(message);
        } else {
            getErrorMessages().add(message);
            FAILED_MESSAGES.set(getErrorMessages());
        }
    }

    private static List<String> getErrorMessages() {
        return FAILED_MESSAGES.get();
    }

    public static void failAll() {
        if (!getErrorMessages().isEmpty()) {
            fail("Test Failed with Below Errors \n" + String.join("\n", getErrorMessages()));
            FAILED_MESSAGES.remove();
        }
    }
}
