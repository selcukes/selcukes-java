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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class Streams {
    /**
     * It takes an iterator and returns a stream.
     *
     * @param iterator The iterator to convert to a stream.
     * @return A stream of the iterator.
     */
    public <T> Stream<T> of(final Iterator<? extends T> iterator) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    /**
     * "Return the index of the first element in the list that matches the predicate, or an empty OptionalInt if no such
     * element exists."
     *
     * The first thing we do is convert the list to a Stream. This is done using the of() method
     *
     * @param elements The list of elements to search through.
     * @param predicate A function that takes an element of the list and returns a boolean.
     * @return OptionalInt
     */
    public <T> OptionalInt indexOf(List<T> elements, IntPredicate predicate) {
        return of(elements)
                .filter(predicate)
                .findFirst();
    }

    /**
     * It returns an IntStream of the indices of the elements in the given list.
     *
     * @param elements The list of elements to iterate over.
     * @return An IntStream of the indexes of the elements in the list.
     */
    public <T> IntStream of(List<T> elements) {
        return of(0, elements.size());
    }

    /**
     * Returns an IntStream of the numbers between start and end, inclusive.
     *
     * @param start The starting value of the range.
     * @param end   The end value (exclusive) for the range to be created
     * @return IntStream
     */
    public <T> IntStream of(int start, int end) {
        return IntStream.range(start, end);
    }

    /**
     * Skip the first row, then for each row, create a map from the headers to
     * the values.
     *
     * @param cells The list of lists of strings that represent the table.
     * @return A list of maps.
     */
    public List<Map<String, String>> listOfMap(List<List<String>> cells) {
        var headers = cells.get(0);
        return cells.stream().skip(1).map(row -> Maps.of(headers, row))
                .collect(Collectors.toList());
    }

    /**
     * It takes a list of maps and returns a map of lists
     *
     * @param listMap The list of maps to be converted.
     * @return A map of lists.
     */
    public <K, V> Map<K, List<V>> mapOfList(List<Map<K, V>> listMap) {
        return listMap.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(
                                Map.Entry::getValue,
                                Collectors.toList()
                        )
                ));
    }
}
