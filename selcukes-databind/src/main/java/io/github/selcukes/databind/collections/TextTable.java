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

package io.github.selcukes.databind.collections;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class TextTable<K, V> {
    private final DataTable<K, V> table;

    private TextTable(DataTable<K, V> table) {
        this.table = table;
    }

    public static <K, V> TextTable<K, V> of(DataTable<K, V> table) {
        return new TextTable<>(table);
    }

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

    public String printHtmlTable() {
        var htmlTable = new HtmlTable<K, V>();
        return htmlTable.buildTable(table);
    }

    private static class HtmlTable<K, V> {

        public String buildTable(DataTable<K, V> table) {
            StringBuilder sb = new StringBuilder();
            sb.append("<table>\n");
            sb.append(buildHeaderRow(table.getFirst()));
            sb.append(buildDataRows(table));
            sb.append("</table>");

            return sb.toString();
        }

        private String buildHeaderRow(Map<K, V> row) {
            StringBuilder sb = new StringBuilder();
            sb.append("<tr>\n");
            row.keySet().forEach(key -> sb.append("<th>").append(key).append("</th>\n"));
            sb.append("</tr>\n");
            return sb.toString();
        }

        private String buildDataRows(DataTable<K, V> table) {
            StringBuilder sb = new StringBuilder();
            table.rows()
                    .map(this::buildDataRow)
                    .forEach(sb::append);
            return sb.toString();
        }

        private String buildDataRow(Map<K, V> row) {
            StringBuilder sb = new StringBuilder();
            sb.append("<tr>\n");
            row.values().forEach(value -> sb.append("<td>").append(value).append("</td>\n"));
            sb.append("</tr>\n");
            return sb.toString();
        }
    }
}
