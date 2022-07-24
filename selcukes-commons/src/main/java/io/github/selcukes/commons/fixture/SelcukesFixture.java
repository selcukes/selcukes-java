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

@UtilityClass
public class SelcukesFixture {
    @Getter
    @Setter
    private String validator;
    @Getter
    @Setter
    private Class<?> reporter;

    public static void fail(String param) {
        try {
            Reflections.invoke(Class.forName(getValidator()), "fail", param);
        } catch (Exception e) {
            throw new AssertionError(e.getCause());
        }
    }

    public static void attach(String param) {
        try {
            Reflections.invoke(getReporter(), "log", param);
        } catch (Exception e) {
            throw new SelcukesException(e.getCause());
        }
    }
}
