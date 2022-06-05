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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class WebClient {
    private HttpClient.Builder clientBuilder;
    private HttpRequest.Builder requestBuilder;
    private BodyPublisher bodyPublisher;

    @SneakyThrows
    public WebClient(String url) {
        clientBuilder = HttpClient.newBuilder();
        requestBuilder = HttpRequest.newBuilder()
            .uri(new URI(url));
    }

    @SneakyThrows
    public Response post(Object payload) {
        contentType("application/json");
        HttpRequest request = requestBuilder.POST(bodyPublisher(payload)).build();
        return execute(request);
    }

    @SneakyThrows
    public Response post() {
        HttpRequest request = requestBuilder.POST(bodyPublisher).build();
        return execute(request);
    }

    public Response delete() {
        HttpRequest request = requestBuilder.DELETE().build();
        return execute(request);
    }

    @SneakyThrows
    private BodyPublisher bodyPublisher(Object payload) {
        BodyPublisher bodyPublisher;
        if (payload instanceof String)
            bodyPublisher = BodyPublishers.ofString(payload.toString());
        else if (payload instanceof Path)
            bodyPublisher = BodyPublishers.ofFile((Path) payload);
        else
            bodyPublisher = BodyPublishers.ofString(StringHelper.toJson(payload));
        return bodyPublisher;
    }


    @SneakyThrows
    private BodyPublisher multiPartBody(Map<Object, Object> data, String boundary) {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary
            + "\r\nContent-Disposition: form-data; name=")
            .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\""
                    + path.getFileName() + "\"\r\nContent-Type: " + mimeType
                    + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(
                    ("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue()
                        + "\r\n").getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return BodyPublishers.ofByteArrays(byteArrays);
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
        String encodedAuth = Base64.getEncoder()
            .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        header("Authorization", "Basic " + encodedAuth);
        return this;
    }

    public WebClient authenticator(String token) {
        header("Authorization", "Bearer " + token);
        return this;
    }

    public WebClient header(String name, String value) {
        requestBuilder = requestBuilder.header(name, value);
        return this;
    }

    public WebClient multiPart(Map<Object, Object> data) {
        String boundary = "-------------" + UUID.randomUUID();
        contentType("multipart/form-data; boundary=" + boundary);
        bodyPublisher = multiPartBody(data, boundary);
        return this;
    }

    public WebClient contentType(String type) {
        header("Content-Type", type);
        return this;
    }

}
