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
     * @param  iterator The iterator to convert to a stream.
     * @return          A stream of the iterator.
     */
    public <T> Stream<T> of(final Iterator<? extends T> iterator) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    /**
     * "Return the index of the first element in the list that matches the
     * predicate, or an empty OptionalInt if no such element exists."
     * <p>
     * The first thing we do is convert the list to a Stream. This is done using
     * the of() method
     *
     * @param  elements  The list of elements to search through.
     * @param  predicate A function that takes an element of the list and
     *                   returns a boolean.
     * @return           OptionalInt
     */
    public <T> OptionalInt indexOf(List<T> elements, IntPredicate predicate) {
        return of(elements)
                .filter(predicate)
                .findFirst();
    }

    /**
     * It returns an IntStream of the indices of the elements in the given list.
     *
     * @param  elements The list of elements to iterate over.
     * @return          An IntStream of the indexes of the elements in the list.
     */
    public <T> IntStream of(List<T> elements) {
        return of(0, elements.size());
    }

    /**
     * Returns an IntStream of the numbers between start and end, inclusive.
     *
     * @param  start The starting value of the range.
     * @param  end   The end value (exclusive) for the range to be created
     * @return       IntStream
     */
    public IntStream of(int start, int end) {
        return IntStream.range(start, end);
    }

    /**
     * Converts a list of lists of strings into a list of maps, where each map
     * represents a row in the table and the keys correspond to the column
     * headers.
     * <p>
     * The first row in the list represents the headers, and is skipped. For
     * each subsequent row, a map is created with keys corresponding to the
     * headers and values corresponding to the cell values in the row. If a cell
     * value is missing, an empty string is used as the default value.
     *
     * @param  cells a list of lists of strings that represent the table
     * @return       a list of maps, where each map represents a row in the
     *               table
     */
    public List<Map<String, String>> toListOfMap(List<List<String>> cells) {
        var headers = cells.get(0);
        return cells.stream().skip(1).map(row -> Maps.of(headers, row, ""))
                .collect(Collectors.toList());
    }

    /**
     * Convert a List of Maps into a Map of Lists, where the keys of each Map in
     * the input List become the keys of the output Map and the values of each
     * Map in the input List are added to the corresponding List in the output
     * Map.
     *
     * @param  listMap A List of Maps to be converted into a Map of Lists.
     * @return         A Map of Lists where each key is mapped to a List of
     *                 values from the input List.
     */
    public <K, V> Map<K, List<V>> toMapOfList(List<Map<K, V>> listMap) {
        return listMap.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                    Map.Entry::getKey,
                    Collectors.mapping(
                        Map.Entry::getValue,
                        Collectors.toList())));
    }

    /**
     * Convert a list of lists of strings into a 2D array of strings.
     *
     * @param  cells The list of lists of strings to convert to a 2D array.
     * @return       A 2D array of Strings
     */
    public String[][] toArray(final List<List<String>> cells) {
        return cells.stream()
                .map(row -> row.toArray(String[]::new))
                .toArray(String[][]::new);
    }

    /**
     * Return a list of trimmed strings from the given list of strings.
     *
     * @param  list The list to trim.
     * @return      A list of strings that have been trimmed.
     */
    public List<String> trim(final List<String> list) {
        return list.stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Returns a stream of string representations of the constants of the given
     * enum class.
     *
     * @param  enumData The enum class whose constants are to be streamed.
     * @return          A stream of string representations of the enum
     *                  constants.
     */
    public Stream<String> of(final Class<? extends Enum<?>> enumData) {
        return Stream.of(enumData.getEnumConstants()).map(Enum::toString);
    }

    /**
     * Groups a list of maps by a specified key.
     *
     * @param  data A list of maps representing the data to group.
     * @param  key  The key to group the data by.
     * @return      A map of lists of maps grouped by the specified key.
     */
    public Map<String, List<Map<String, String>>> groupBy(List<Map<String, String>> data, String key) {
        return data.stream()
                .filter(map -> map.containsKey(key))
                .collect(Collectors.groupingBy(map -> map.get(key), Collectors.toList()));
    }

}
