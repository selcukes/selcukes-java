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

import javax.net.ssl.SSLContext;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class WebClient {
    private final HttpClient.Builder clientBuilder = HttpClient.newBuilder();
    private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    private BodyPublisher bodyPublisher;
    @Singular
    private final Map<String, String> cookies = new ConcurrentHashMap<>();
    @Singular
    private final Map<String, String> queryParams = new ConcurrentHashMap<>();
    private final String baseUri;
    private String endpoint;

    public WebClient(String baseUri) {
        this.baseUri = Objects.requireNonNull(baseUri, "baseUri must not be null").trim();
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
     * Adds cookies to the request.
     *
     * @param  cookiesMap The map containing cookies.
     * @return            The WebClient object.
     */
    public WebClient cookies(Map<String, String> cookiesMap) {
        cookies.putAll(cookiesMap);
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
     * Executes an HTTP GET request.
     *
     * @return A Response object representing the server's response.
     */
    public WebResponse get() {
        requestBuilder.GET();
        return execute();
    }

    /**
     * Executes an HTTP POST request with a JSON payload.
     * <p>
     * This method serializes the provided object to JSON and sends it as the
     * body of a POST request. The content type of the request is set to
     * "application/json," which is a standard content type for JSON data.
     * </p>
     *
     * @param  payload The object to be serialized and sent as the request body.
     * @return         A Response object representing the server's response.
     */
    public WebResponse post(final Object payload) {
        contentType("application/json")
                .body(payload);
        requestBuilder.POST(bodyPublisher);
        return execute();
    }

    /**
     * Executes an HTTP POST request without a specific payload.
     *
     * @return A Response object representing the server's response.
     */
    public WebResponse post() {
        requestBuilder.POST(bodyPublisher);
        return execute();
    }

    /**
     * Executes an HTTP DELETE request.
     *
     * @return A Response object representing the server's response.
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

    /**
     * Creates a PUT request without a specific payload and executes it.
     * <p>
     * This method sends a PUT request to the server without attaching a
     * specific payload.
     * </p>
     *
     * @return A Response object representing the server's response.
     */
    public WebResponse put() {
        requestBuilder.PUT(bodyPublisher);
        return execute();
    }

    /**
     * Creates a PATCH request with the given payload and executes it.
     * <p>
     * This method sets the request body with the provided payload and sends a
     * PATCH request to the server. The payload can be of various types, such as
     * String, Path, or any object that can be serialized to JSON.
     * </p>
     *
     * @param  payload The payload to be sent to the server.
     * @return         A Response object representing the server's response.
     */
    public WebResponse patch(final Object payload) {
        body(payload);
        requestBuilder.method("PATCH", bodyPublisher);
        return execute();
    }

    /**
     * Creates a PATCH request without a specific payload and executes it.
     * <p>
     * This method sends a PATCH request to the server without attaching a
     * specific payload.
     * </p>
     *
     * @return A Response object representing the server's response.
     */
    public WebResponse patch() {
        requestBuilder.method("PATCH", bodyPublisher);
        return execute();
    }

    /**
     * Executes an HTTP OPTIONS request.
     * <p>
     * This method sends an HTTP OPTIONS request to the server.
     * </p>
     *
     * @return A Response object representing the server's response.
     */
    public WebResponse options() {
        requestBuilder.method("OPTIONS", bodyPublisher);
        return execute();
    }

    @SneakyThrows
    private BodyPublisher bodyPublisher(final Object payload) {
        if (payload instanceof String payloadString) {
            bodyPublisher = BodyPublishers.ofString(payloadString);
        } else if (payload instanceof Path filePath) {
            bodyPublisher = BodyPublishers.ofFile(filePath);
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

            if (entry.getValue() instanceof Path path) {
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
            requestBuilder.header("Cookie", cookieHeader);
            clientBuilder.followRedirects(HttpClient.Redirect.NORMAL);
        }
        var request = requestBuilder.uri(buildUri()).build();
        var httpResponse = clientBuilder.build().send(request, ofString());
        var response = new WebResponse(httpResponse);
        response.logIfError();
        return response;
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
        url.ifPresent(u -> clientBuilder
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
        requestBuilder.header(name, value);
        return this;
    }

    /**
     * Adds headers to the request.
     *
     * @param  headersMap The map containing headers.
     * @return            The WebClient object.
     */
    public WebClient headers(Map<String, String> headersMap) {
        headersMap.forEach(this::header);
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

    /**
     * /** Adds query parameters to the request.
     * <p>
     * This method allows you to include multiple query parameters in your HTTP
     * request. The provided map is processed, and its key-value pairs are
     * encoded and added to the query parameters map.
     * </p>
     *
     * @param  parameters The map containing query parameters.
     * @return            The {@code WebClient} object with the specified query
     *                    parameters added.
     */
    public WebClient queryParams(Map<String, String> parameters) {
        parameters.forEach(this::queryParams);
        return this;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private URI buildUri() {
        var joiner = new StringJoiner("&");
        queryParams.forEach((key, value) -> joiner.add(encode(key) + "=" + encode(value)));
        return URI.create(baseUri + endpoint + (baseUri.contains("?") ? "&" : "?") + joiner);
    }

    /**
     * Executes an HTTP request based on the provided method.
     *
     * @param  method   The HTTP method (GET, POST, PUT, DELETE, etc.).
     * @param  endpoint The API endpoint.
     * @return          A Response object.
     */
    public WebResponse executeRequest(String method, String endpoint) {
        return switch (method.toUpperCase()) {
            case "GET" -> endpoint(endpoint).get();
            case "POST" -> endpoint(endpoint).post();
            case "PUT" -> endpoint(endpoint).put();
            case "PATCH" -> endpoint(endpoint).patch();
            case "OPTIONS" -> endpoint(endpoint).options();
            case "DELETE" -> endpoint(endpoint).delete();
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }

    /**
     * Configures the SSLContext for the WebClient.
     * <p>
     * This method allows you to set a custom SSLContext for the underlying HTTP
     * client used by the WebClient. SSLContext is responsible for managing the
     * SSL/TLS configuration, including certificates and security settings.
     * </p>
     *
     * @param  sslContext               The SSLContext to be set for the
     *                                  WebClient.
     * @return                          A reference to the current WebClient
     *                                  instance to support method chaining.
     * @throws IllegalArgumentException If the provided SSLContext is null.
     * @see                             javax.net.ssl.SSLContext
     */
    public WebClient sslContext(SSLContext sslContext) {
        clientBuilder.sslContext(sslContext);
        return this;
    }

}
