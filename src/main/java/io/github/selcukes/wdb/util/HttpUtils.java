/*
 *
 *  * Copyright (c) Ramesh Babu Prudhvi.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.selcukes.wdb.util;

import io.github.selcukes.wdb.exception.WebDriverBinaryException;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Optional;
import java.util.function.Function;

import static io.github.selcukes.wdb.util.OptionalUtil.unwrap;

public final class HttpUtils {
    private static String proxy;

    private HttpUtils() {

    }

    private static Function<String, HttpURLConnection> connection = endpoint -> {
        HttpURLConnection.setFollowRedirects(false);

        final HttpURLConnection httpURLConnection;

        try {
            URL url = new URL(endpoint);
            httpURLConnection = isProxy().isPresent() ? (HttpURLConnection) url.openConnection(unwrap(isProxy())) : (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            return httpURLConnection;
        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        } finally {
            HttpURLConnection.setFollowRedirects(true);
        }
    };

    public static String getLocation(String endpoint, String proxyUrl) {
        proxy = proxyUrl;
        return extract(connection.apply(endpoint), t -> t.getHeaderField("Location"));
    }

    public static InputStream getResponseInputStream(String endpoint, String proxyUrl) {
        proxy = proxyUrl;
        return extract(connection.apply(endpoint), t -> {
            try {
                return t.getInputStream();
            } catch (IOException e) {
                throw new WebDriverBinaryException(e);
            }
        });
    }

    private static <T> T extract(HttpURLConnection connection, Function<HttpURLConnection, T> condition) {
        return condition.apply(connection);
    }

    public static Optional<Proxy> isProxy() {
        Optional<URL> url = getProxyUrl(proxy);
        if (url.isPresent()) {
            String proxyHost = url.get().getHost();
            int proxyPort = url.get().getPort() == -1 ? 80
                : url.get().getPort();
            return Optional.of(new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(proxyHost, proxyPort)));
        }
        return Optional.empty();
    }

    public static Optional<URL> getProxyUrl(String proxy) {
        try {
            return Optional.of(new URL(proxy));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }

}