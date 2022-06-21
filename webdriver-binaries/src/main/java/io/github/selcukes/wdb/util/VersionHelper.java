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

package io.github.selcukes.wdb.util;

import io.github.selcukes.commons.http.Response;
import io.github.selcukes.commons.http.WebClient;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.selcukes.commons.xml.XmlMapper.filterElements;
import static io.github.selcukes.commons.xml.XmlMapper.selectElements;

public class VersionHelper {
    private VersionHelper() {

    }

    public static Response sendRequest(String binaryDownloadUrl, String proxy) {
        return new WebClient(binaryDownloadUrl).proxy(proxy).get();
    }

    public static Map<String, String> versionsMap(String url, String expression, String matcher) {
        try {
            var xmlDocument = sendRequest(url, null)
                .bodyXml();
            var elements = selectElements(xmlDocument, expression);
            var versions = filterElements(elements, matcher);
            return versions
                .collect(Collectors.toMap(
                    entry -> entry.substring(0, entry.indexOf('/')),
                    entry -> entry,
                    (prev, next) -> next, TreeMap::new
                ));
        } catch (Exception e) {
            return Collections.emptyMap();
        }

    }

    public static List<String> versionsList(String url, String expression, String matcher) {
        try {
            return new ArrayList<>(versionsMap(url, expression, matcher).keySet());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
