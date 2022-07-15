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

package io.github.selcukes.commons;

import io.github.selcukes.commons.annotation.Lifecycle;
import io.github.selcukes.commons.helper.ServiceLoaderUtils;
import io.github.selcukes.commons.listener.LifecycleManager;
import io.github.selcukes.commons.listener.TestLifecycleListener;
import lombok.experimental.UtilityClass;

import static java.util.Optional.ofNullable;

@UtilityClass
public class SelcukesLifecycle {
    public static LifecycleManager getDefaultLifecycle() {
        final var classLoader = Thread.currentThread().getContextClassLoader();
        var listeners = ServiceLoaderUtils.load(TestLifecycleListener.class, classLoader);
        return new LifecycleManager(listeners);
    }

    public static <T> Lifecycle.Type getLifecycleType(Class<T> clazz) {
        return ofNullable(clazz.getDeclaredAnnotation(Lifecycle.class))
            .map(Lifecycle::type)
            .orElse(Lifecycle.Type.NONE);
    }
}
