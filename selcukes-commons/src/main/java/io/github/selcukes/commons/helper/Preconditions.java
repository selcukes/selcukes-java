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

package io.github.selcukes.commons.helper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Preconditions {
    /**
     * Checks the given boolean condition, and throws an
     * {@code IllegalArgumentException} if the condition is not met (evaluates
     * to {@code false}). The exception will have the given error message.
     *
     * @param  condition                The condition to check
     * @param  errorMessage             The message for the
     *                                  {@code IllegalArgumentException} that is
     *                                  thrown if the check fails.
     * @throws IllegalArgumentException Thrown, if the condition is violated.
     */
    public void checkArgument(final boolean condition, final String errorMessage) {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
