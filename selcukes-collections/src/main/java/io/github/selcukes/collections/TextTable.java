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

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TextTable<K, V> {
    private final DataTable<K, V> table;

    private TextTable(DataTable<K, V> table) {
        this.table = table;
    }

    public static <K, V> TextTable<K, V> of(DataTable<K, V> table) {
        return new TextTable<>(table);
    }

    @SuppressWarnings("squid:S3457")
    public String printTable() {
        var columnWidths = table.stream()
                .flatMap(row -> row.keySet().stream())
                .distinct()
                .collect(Maps.of(
                    key -> key,
                    key -> Math.max(
                        key.toString().length(),
                        table.stream()
                                .map(row -> row.get(key))
                                .filter(Objects::nonNull)
                                .map(Object::toString)
                                .map(String::length)
                                .max(Integer::compareTo)
                                .orElse(0))));

        var header = columnWidths.keySet().stream()
                .map(key -> String.format("| %-" + columnWidths.get(key) + "s ", key))
                .collect(Collectors.joining());

        var separator = "+-" + columnWidths.values().stream()
                .map("-"::repeat)
                .collect(Collectors.joining("-+-")) + "-+";

        var rows = table.stream()
                .map(row -> columnWidths.keySet().stream()
                        .map(key -> String.format("| %-" + columnWidths.get(key) + "s ", row.get(key)))
                        .collect(Collectors.joining()))
                .map(row -> row + "|")
                .collect(Collectors.joining("\n"));

        return separator + "\n" + header + "|\n" + separator + "\n" + rows + "\n" + separator + "\n";
    }

    public String printCSV() {
        return Stream.concat(
            Stream.of(getCSVRow(table.getColumns())),
            table.stream().map(row -> getCSVRow(row.values())))
                .collect(Collectors.joining("\n"));
    }

    private <T> String getCSVRow(Collection<T> values) {
        return values.stream()
                .map(Object::toString)
                .map(this::escapeCsvValue)
                .collect(Collectors.joining(","));
    }

    private String escapeCsvValue(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public String printHtmlTable() {
        var htmlTable = new HtmlTable<K, V>();
        return htmlTable.buildTable(table);
    }

    private static class HtmlTable<K, V> {

        public String buildTable(DataTable<K, V> table) {
            return "<table>\n" +
                    buildHeaderRow(table.getFirst()) +
                    buildDataRows(table) +
                    "</table>";
        }

        private String buildHeaderRow(Map<K, V> row) {
            return buildRow(row.keySet(), "<th>");
        }

        private String buildDataRows(DataTable<K, V> table) {
            return table.rows()
                    .map(this::buildDataRow)
                    .collect(Collectors.joining());
        }

        private String buildDataRow(Map<K, V> row) {
            return buildRow(row.values(), "<td>");
        }

        private <T> String buildRow(Collection<T> values, String tag) {
            return "<tr>\n" +
                    values.stream()
                            .map(value -> String.format("%s%s%s", tag, value, tag))
                            .collect(Collectors.joining("\n"))
                    +
                    "</tr>\n";
        }
    }
}
