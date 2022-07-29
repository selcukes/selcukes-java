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
     * Ensures that the given object reference is not null. Upon violation, a
     * {@code NullPointerException} with the given message is thrown.
     *
     * @param  reference            The object reference
     * @param  errorMessage         The message for the
     *                              {@code NullPointerException} that is thrown
     *                              if the check fails.
     * @return                      The object reference itself (generically
     *                              typed).
     * @throws NullPointerException Thrown, if the passed reference was null.
     */
    public <T> T checkNotNull(final T reference, final String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        }
        return reference;
    }

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

    /**
     * Checks the given boolean condition, and throws an
     * {@code IllegalStateException} if the condition is not met (evaluates to
     * {@code false}). The exception will have the given error message.
     *
     * @param  condition             The condition to check
     * @param  errorMessage          The message for the
     *                               {@code IllegalStateException} that is
     *                               thrown if the check fails.
     * @throws IllegalStateException Thrown, if the condition is violated.
     */
    public void checkState(final boolean condition, final String errorMessage) {
        if (!condition) {
            throw new IllegalStateException((errorMessage));
        }
    }
}
