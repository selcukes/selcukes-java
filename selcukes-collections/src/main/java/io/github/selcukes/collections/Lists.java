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

package io.github.selcukes.collections;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class Lists {

    /**
     * Returns a new {@code LinkedList} instance containing the specified
     * elements in the same order as they appear in the input array. This method
     * is annotated with {@code @SafeVarargs} to suppress unchecked warnings
     * that would otherwise occur due to the use of a varargs parameter.
     *
     * @param  <E>      the type of elements in the list
     * @param  elements the elements to be added to the list
     * @return          a new LinkedList containing the specified elements
     */
    @SafeVarargs
    public <E> List<E> of(E... elements) {
        return new LinkedList<>(Arrays.asList(elements));
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
        return StringHelper.isNonEmpty(value.toString());
    }

    /**
     * Returns a new case-insensitive list containing the specified elements.
     *
     * @param  elements the elements to include in the list
     * @return          a new case-insensitive list containing the specified
     *                  elements
     */
    public List<String> ofIgnoreCase(String... elements) {
        var stringList = of(elements);
        return new CaseInsensitiveList(stringList);
    }

    /**
     * Returns a new list containing only the elements of the given list that
     * match the specified predicate.
     *
     * @param  <T>                  the type of elements in the list
     * @param  list                 the list to be filtered
     * @param  predicate            the predicate used to test elements for
     *                              inclusion
     * @return                      a new list containing only the matching
     *                              elements
     * @throws NullPointerException if the given list is {@code null}.
     */
    public <T> List<T> retainIf(@NonNull List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Sorts a list of elements with proper handling of null values.
     *
     * @param  list the list of elements to be sorted
     * @param  <T>  the type of elements in the list
     * @return      a new list containing the sorted elements with nulls placed
     *              at the end
     */
    public <T> List<T> sortWithNulls(List<T> list) {
        return list.stream()
                .sorted(Comparator.comparing(Object::toString, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of string representations of the objects in the given
     * list.
     *
     * @param  list                 the list of objects to convert to strings
     * @param  <T>                  the type of object in the list
     * @return                      a list of string representations of the
     *                              objects in the given list.
     * @throws NullPointerException if the given list is {@code null}.
     */
    public <T> List<String> toString(@NonNull List<T> list) {
        return list.stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    @NoArgsConstructor
    private static class CaseInsensitiveList extends ArrayList<String> {

        public CaseInsensitiveList(final Collection<String> collection) {
            super(collection);
        }

        @Override
        public boolean contains(final Object o) {
            final String s = (String) o;
            return this.stream().anyMatch(str -> str.equalsIgnoreCase(s));
        }
    }
}
