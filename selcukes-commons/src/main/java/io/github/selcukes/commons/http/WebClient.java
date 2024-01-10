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

import io.github.selcukes.collections.Maps;
import io.github.selcukes.collections.Resources;
import io.github.selcukes.databind.utils.JsonUtils;
import lombok.Singular;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class WebClient {
    private HttpClient.Builder clientBuilder;
    private HttpRequest.Builder requestBuilder;
    private BodyPublisher bodyPublisher;
    @Singular
    private final Map<String, String> cookies;
    @Singular
    private final Map<String, String> queryParams;
    private final String baseUri;
    private String endpoint;

    public WebClient(String baseUri) {
        clientBuilder = HttpClient.newBuilder();
        requestBuilder = HttpRequest.newBuilder();
        queryParams = new ConcurrentHashMap<>();
        cookies = new ConcurrentHashMap<>();
        this.baseUri = Objects.requireNonNull(baseUri, "baseUri must not be null");
        this.endpoint = "";
    }

    /**
     * Sets the endpoint for the request.
     *
     * @param  endpoint The endpoint to set.
     * @return          The WebClient object.
     */
    public WebClient endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Adds a cookie to the request.
     *
     * @param  name  The name of the cookie.
     * @param  value The value of the cookie.
     * @return       The WebClient object.
     */
    public WebClient cookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    /**
     * Sets the body of the request.
     *
     * @param  payload The payload to be set as the request body.
     * @return         The WebClient object.
     */
    public WebClient body(final Object payload) {
        this.bodyPublisher = bodyPublisher(payload);
        return this;
    }

    /**
     * This function creates a GET request and executes it.
     *
     * @return A Response object.
     */
    public WebResponse get() {
        requestBuilder.GET();
        return execute();
    }

    /**
     * "This function takes an object, serializes it to JSON, and sends it to
     * the server as the body of a POST request."
     * <p>
     * The first line of the function sets the content type of the request to
     * "application/json". This is a standard content type for JSON data
     *
     * @param  payload The object to be serialized and sent as the request body.
     * @return         A Response object
     */
    @SneakyThrows
    public WebResponse post(final Object payload) {
        contentType("application/json")
                .body(payload);
        requestBuilder.POST(bodyPublisher);
        return execute();
    }

    /**
     * This function creates a POST request with the given body and executes it.
     *
     * @return A Response object.
     */
    @SneakyThrows
    public WebResponse post() {
        requestBuilder.POST(bodyPublisher);
        return execute();
    }

    /**
     * This function builds a DELETE request and executes it.
     *
     * @return A Response object.
     */
    public WebResponse delete() {
        requestBuilder.DELETE();
        return execute();
    }

    /**
     * "Create a PUT request with the given payload, and execute it."
     * <p>
     * The first line creates a new `HttpRequest` object. The `requestBuilder`
     * object is a member variable of the `HttpClient` class. It's a
     * `HttpRequest.Builder` object, and it's used to create new `HttpRequest`
     * objects
     *
     * @param  payload The payload to be sent to the server.
     * @return         A Response object
     */
    public WebResponse put(final Object payload) {
        body(payload);
        requestBuilder.PUT(bodyPublisher);
        return execute();
    }

    @SneakyThrows
    private BodyPublisher bodyPublisher(final Object payload) {
        if (payload instanceof String) {
            bodyPublisher = BodyPublishers.ofString(payload.toString());
        } else if (payload instanceof Path) {
            bodyPublisher = BodyPublishers.ofFile((Path) payload);
        } else {
            bodyPublisher = BodyPublishers.ofString(JsonUtils.toJson(payload));
        }
        return bodyPublisher;
    }

    @SneakyThrows
    private <K, V> BodyPublisher multiPartBody(final Map<K, V> data, final String boundary) {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary
                + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (var entry : data.entrySet()) {
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
    private WebResponse execute() {
        if (!cookies.isEmpty()) {
            String cookieHeader = Maps.join(cookies, "=", "; ");
            requestBuilder = requestBuilder.header("Cookie", cookieHeader);
            clientBuilder.followRedirects(HttpClient.Redirect.NORMAL);
        }
        var request = requestBuilder.uri(buildUri()).build();
        return new WebResponse(clientBuilder.build().send(request, ofString()));
    }

    /**
     * If the proxy parameter is a valid URL, then set the proxy host and port
     * to the host and port of the URL
     *
     * @param  proxy The proxy to use.
     * @return       A WebClient object
     */
    public WebClient proxy(final String proxy) {
        var url = Resources.tryURL(proxy);
        url.ifPresent(u -> clientBuilder = clientBuilder
                .proxy(ProxySelector.of(new InetSocketAddress(u.getHost(),
                    u.getPort() == -1 ? 80 : u.getPort()))));
        return this;
    }

    /**
     * This function takes a username and password, encodes them in base64, and
     * adds them to the header of the request.
     *
     * @param  username The username to use for authentication
     * @param  password The password to use for authentication
     * @return          The WebClient object itself.
     */
    public WebClient authenticator(final String username, final String password) {
        String encodedAuth = Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        header("Authorization", "Basic " + encodedAuth);
        return this;
    }

    /**
     * This function adds an Authorization header to the request with the value
     * of Bearer Token.
     *
     * @param  token The token you received from the authentication service.
     * @return       The WebClient object.
     */
    public WebClient authenticator(final String token) {
        header("Authorization", "Bearer " + token);
        return this;
    }

    /**
     * This function adds a header to the request.
     *
     * @param  name  The name of the header.
     * @param  value The value of the header.
     * @return       The WebClient object
     */
    public WebClient header(final String name, final String value) {
        requestBuilder = requestBuilder.header(name, value);
        return this;
    }

    /**
     * "Set the body of the request to be a multipart form with the given data
     * and boundary."
     * <p>
     * The first thing we do is generate a random boundary. This is a string
     * that will be used to separate the different parts of the multipart form
     *
     * @param  data The data to be sent.
     * @return      A WebClient object
     */
    public <K, V> WebClient multiPart(final Map<K, V> data) {
        String boundary = "-------------" + UUID.randomUUID();
        contentType("multipart/form-data; boundary=" + boundary);
        bodyPublisher = multiPartBody(data, boundary);
        return this;
    }

    /**
     * This function sets the content type of the request to the given type.
     *
     * @param  type The content type to set.
     * @return      The WebClient object
     */
    public WebClient contentType(final String type) {
        header("Content-Type", type);
        return this;
    }

    /**
     * Adds a query parameter to the request.
     * <p>
     * This method allows you to include query parameters in your HTTP request.
     * The provided name and value are encoded and added to the query parameters
     * map.
     * </p>
     *
     * @param  name  The name of the query parameter.
     * @param  value The value of the query parameter.
     * @return       The {@code WebClient} object with the specified query
     *               parameter added.
     */
    public WebClient queryParams(String name, String value) {
        queryParams.put(encode(name), encode(value));
        return this;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private URI buildUri() {
        var joiner = new StringJoiner("&");
        queryParams.forEach((key, value) -> joiner.add(encode(key) + "=" + encode(value)));
        return Resources.toURI(baseUri + endpoint + (baseUri.contains("?") ? "&" : "?") + joiner);
    }
}
