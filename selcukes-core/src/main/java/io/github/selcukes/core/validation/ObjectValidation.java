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

import static io.github.selcukes.core.validation.Validation.failWithMessage;

@CustomLog
public class ObjectValidation {
    private final Object actual;
    private final boolean isSoft;

    public ObjectValidation(boolean isSoft, Object object) {
        this.isSoft = isSoft;
        this.actual = object;
    }

    public ObjectValidation isEqualTo(Object expected) {
        logger.info(() -> String.format("Verifying Object should be [%s] ", expected));
        if (!actual.equals(expected)) {
            failWithMessage(isSoft, "Expected Object should be [%s] but was [%s]", expected, actual);
        }
        return this;
    }

    public ObjectValidation isNotEqualTo(Object expected) {
        logger.info(() -> String.format("Verifying Object should not be [%s] ", expected));
        if (!actual.equals(expected)) {
            failWithMessage(isSoft, "Expected Object should not be [%s] but was [%s]", expected, actual);
        }
        return this;
    }
}
