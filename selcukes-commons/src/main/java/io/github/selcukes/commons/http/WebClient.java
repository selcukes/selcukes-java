/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.http;


import io.github.selcukes.databind.utils.StringHelper;
import lombok.SneakyThrows;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.util.Optional;

import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class WebClient {
    private HttpClient.Builder clientBuilder;
    private HttpRequest.Builder requestBuilder;

    @SneakyThrows
    public WebClient(String url) {
        clientBuilder = HttpClient.newBuilder();
        requestBuilder = HttpRequest.newBuilder(new URI(url))
            .header("Content-Type", "application/json")
            .version(HttpClient.Version.HTTP_2);
    }

    @SneakyThrows
    public Response post(Object payload) {
        BodyPublisher bodyPublisher;
        if (payload instanceof String)
            bodyPublisher = BodyPublishers.ofString(payload.toString());
        else if (payload instanceof Path)
            bodyPublisher = BodyPublishers.ofFile((Path) payload);
        else
            bodyPublisher = BodyPublishers.ofString(StringHelper.toJson(payload));
        HttpRequest request = requestBuilder.POST(bodyPublisher).build();
        return execute(request);
    }

    @SneakyThrows
    private Response execute(HttpRequest request) {
        return new Response(clientBuilder.build().send(request, ofString()));
    }

    @SneakyThrows
    public Response sendRequest() {
        HttpRequest request = requestBuilder.GET().build();
        return execute(request);
    }

    private Optional<URL> getProxyUrl(String proxy) {
        try {
            return Optional.of(new URL(proxy));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }

    public WebClient proxy(String proxy) {
        Optional<URL> url = getProxyUrl(proxy);
        if (url.isPresent()) {
            String proxyHost = url.get().getHost();
            int proxyPort = url.get().getPort() == -1 ? 80
                : url.get().getPort();
            clientBuilder = clientBuilder
                .proxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)));
        }
        return this;
    }

    public WebClient authenticator(String username, String password) {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        };
        clientBuilder = clientBuilder.authenticator(authenticator);
        return this;
    }

    public WebClient authenticator(String token) {
        requestBuilder = requestBuilder.header("Authorization", "Bearer " + token);
        return this;
    }
}
