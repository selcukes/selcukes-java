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

/**
 * It's a class that implements the `Lifecycle` interface
 */
@UtilityClass
public class SelcukesLifecycle {
    /**
     * "Get the default lifecycle manager by loading all the test lifecycle
     * listeners from the classpath."
     * <p>
     * The first line of the function gets the current thread's context class
     * loader. This is the class loader that was used to load the class that
     * contains the function
     *
     * @return A new instance of LifecycleManager
     */
    public LifecycleManager getDefaultLifecycle() {
        final var classLoader = Thread.currentThread().getContextClassLoader();
        var listeners = ServiceLoaderUtils.load(TestLifecycleListener.class, classLoader);
        return new LifecycleManager(listeners);
    }

    /**
     * "If the class has a Lifecycle annotation, return the type of the
     * annotation, otherwise return NONE."
     * <p>
     * The first thing we do is get the annotation from the class. We use the
     * ofNullable method from the Optional class to wrap the result of the
     * getDeclaredAnnotation method. This is because the getDeclaredAnnotation
     * method can return null
     *
     * @param  clazz The class to check for the annotation.
     * @return       The lifecycle type of the class.
     */
    public <T> Lifecycle.Type getLifecycleType(final Class<T> clazz) {
        return ofNullable(clazz.getDeclaredAnnotation(Lifecycle.class))
                .map(Lifecycle::type)
                .orElse(Lifecycle.Type.NONE);
    }
}
