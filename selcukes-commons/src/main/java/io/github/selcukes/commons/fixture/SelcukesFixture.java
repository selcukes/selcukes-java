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

package io.github.selcukes.commons.fixture;

import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.databind.utils.Reflections;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * It's a class that can be used to create a new instance of the class under
 * test
 */
@UtilityClass
public class SelcukesFixture {
    @Getter
    @Setter
    private String validator;
    @Getter
    @Setter
    private Class<?> reporter;

    /**
     * It calls the fail method of the class returned by getValidator
     *
     * @param param The parameter to be validated.
     */
    public static void fail(final String param) {
        try {
            Reflections.invoke(Class.forName(getValidator()), "fail", param);
        } catch (Exception e) {
            throw new AssertionError(e.getCause());
        }
    }

    /**
     * It invokes the log method of the reporter object with the given parameter
     *
     * @param param The parameter to be attached to the report.
     */
    public static void attach(final String param) {
        try {
            Reflections.invoke(getReporter(), "log", param);
        } catch (Exception e) {
            throw new SelcukesException(e.getCause());
        }
    }
}
