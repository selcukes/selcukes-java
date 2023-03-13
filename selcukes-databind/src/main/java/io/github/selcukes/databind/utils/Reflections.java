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

package io.github.selcukes.databind.utils;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Reflections is a class that provides a way to get a list of all the classes
 * in a package
 */
@UtilityClass
public class Reflections {
    /**
     * Creates a new instance of the class passed to it.
     *
     * @param  clazz                    the class to instantiate
     * @param  initArgs                 the arguments to pass to the constructor
     * @param  <T>                      the type of the class to instantiate
     * @return                          an instance of the class
     * @throws IllegalArgumentException if an error occurs while creating the
     *                                  instance
     */
    @SuppressWarnings("all")
    public static <T> T newInstance(final Class<T> clazz, final Object... initArgs) {
        try {
            var constructor = clazz.getDeclaredConstructor(getClasses(initArgs));
            if (!constructor.canAccess(null)) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(initArgs);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets the value of the field in the object.
     *
     * @param  object                   the object to modify
     * @param  fieldName                the name of the field to modify
     * @param  value                    the new value of the field
     * @throws IllegalArgumentException if an error occurs while setting the
     *                                  field
     */
    @SuppressWarnings("squid:S3011")
    public static void setField(final Object object, final String fieldName, final Object value) {
        try {
            var clazz = object == null ? Object.class : object.getClass();
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * Gets the method from the class.
     *
     * @param  clazz                    the class to get the method from
     * @param  name                     the name of the method to get
     * @param  param                    the parameter types of the method to get
     * @param  <T>                      the type of the class to get the method
     *                                  from
     * @return                          the method from the class
     * @throws IllegalArgumentException if an error occurs while getting the
     *                                  method
     */
    @SuppressWarnings("squid:S3011")
    public static <T> Method getDeclaredMethod(Class<T> clazz, String name, Class<?>... param) {
        try {
            var method = clazz.getDeclaredMethod(name, param);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Invokes a static method on a class with a single string parameter.
     *
     * @param  clazz                    the class containing the method
     * @param  methodName               the name of the method to invoke
     * @param  param                    the string parameter to pass to the
     *                                  method
     * @throws IllegalArgumentException if the method cannot be found, accessed
     *                                  or invoked
     */
    public static void invokeStaticMethod(final Class<?> clazz, final String methodName, final String param) {
        try {
            var method = clazz.getMethod(methodName, String.class);
            method.invoke(null, param);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Class<?>[] getClasses(Object... objects) {
        return Arrays.stream(objects)
                .map(Object::getClass)
                .toArray(Class[]::new);
    }
}
