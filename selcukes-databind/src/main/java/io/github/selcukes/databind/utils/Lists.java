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

import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class Lists {

    /**
     * Creates a new {@code LinkedList} instance containing the specified
     * elements in the same order as they appear in the input array. This method
     * is annotated with {@code @SafeVarargs} to suppress unchecked warnings
     * that would otherwise occur due to the use of a varargs parameter.
     *
     * @param    <E>                      the type of elements in the list
     * @param    elements                 the elements to include in the list
     * @return                            a new {@code LinkedList} containing
     *                                    the specified elements
     * @throws   NullPointerException     if the specified array is {@code null}
     *                                    or contains {@code null} elements
     * @throws   IllegalArgumentException if the specified array is empty
     * @implNote                          This method does not modify the input
     *                                    array or expose it to external code,
     *                                    which makes the use of
     *                                    {@code @SafeVarargs} safe in this
     *                                    context.
     * @see                               List#of(Object[])
     */
    @SafeVarargs
    public <E> List<E> of(E... elements) {
        return new LinkedList<>(List.of(elements));
    }

    /**
     * Returns a list of non-empty values from a stream by applying a mapper
     * function to each element.
     *
     * @param  stream the stream of elements to be mapped and filtered
     * @param  mapper the function to apply to each element in the stream
     * @param  <T>    the type of the input elements in the stream
     * @param  <R>    the type of the output elements in the resulting list
     * @return        a list of non-empty values produced by applying the mapper
     *                function to the elements of the input stream
     */
    public <T, R> List<R> of(Stream<T> stream, Function<? super T, ? extends R> mapper) {
        return stream.parallel()
                .map(mapper)
                .filter(Lists::nonEmpty)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Checks if a given value is non-empty.
     *
     * @param  value the value to be checked for emptiness
     * @param  <T>   the type of the input value
     * @return       true if the value is not empty, false otherwise
     */
    public <T> boolean nonEmpty(T value) {
        if (value instanceof Collection) {
            return !((Collection<?>) value).isEmpty();
        }
        if (value instanceof Object[]) {
            return ((Object[]) value).length > 0;
        }
        return !StringHelper.isNullOrEmpty(value.toString());
    }

    /**
     * Returns a new case-insensitive list containing the specified elements.
     *
     * @param  elements the elements to include in the list
     * @return          a new case-insensitive list containing the specified
     *                  elements
     */
    public List<String> ofIgnoreCase(String... elements) {
        var stringList = Arrays.asList(elements);
        return new IgnoreCaseList(stringList);
    }

    @NoArgsConstructor
    private static class IgnoreCaseList extends ArrayList<String> {

        public IgnoreCaseList(final List<String> stringList) {
            super(stringList);
        }

        @Override
        public boolean contains(Object o) {
            return this.stream().anyMatch(s -> s.equalsIgnoreCase(o.toString()));
        }
    }
}
