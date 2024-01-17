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

import com.fasterxml.jackson.databind.JsonNode;
import io.github.selcukes.commons.exception.SelcukesException;
import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.utils.JsonUtils;
import io.github.selcukes.databind.xml.XmlMapper;
import lombok.Getter;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * It takes the HTTP response and provides a number of methods to access the
 * response body, headers, and status code
 */
@Getter
public class WebResponse {
    private final HttpResponse<String> httpResponse;

    public WebResponse(final HttpResponse<String> httpResponse) {
        this.httpResponse = httpResponse;
        logIfError();
    }

    /**
     * It returns the reason phrase for a given HTTP status code
     *
     * @param  statusCode The HTTP status code.
     * @return            The reason phrase for the given status code.
     */
    public static String getReasonPhrase(final int statusCode) {
        switch (statusCode) {
            case (200):
                return "OK";
            case (201):
                return "Created";
            case (202):
                return "Accepted";
            case (203):
                return "Non Authoritative Information";
            case (204):
                return "No Content";
            case (205):
                return "Reset Content";
            case (206):
                return "Partial Content";
            case (207):
                return "Partial Update OK";
            case (300):
                return "Mutliple Choices";
            case (301):
                return "Moved Permanently";
            case (302):
                return "Moved Temporarily";
            case (303):
                return "See Other";
            case (304):
                return "Not Modified";
            case (305):
                return "Use Proxy";
            case (307):
                return "Temporary Redirect";
            case (400):
                return "Bad Request";
            case (401):
                return "Unauthorized";
            case (402):
                return "Payment Required";
            case (403):
                return "Forbidden";
            case (404):
                return "Not Found";
            case (405):
                return "Method Not Allowed";
            case (406):
                return "Not Acceptable";
            case (407):
                return "Proxy Authentication Required";
            case (408):
                return "Request Timeout";
            case (409):
                return "Conflict";
            case (410):
                return "Gone";
            case (411):
                return "Length Required";
            case (412):
                return "Precondition Failed";
            case (413):
                return "Request Entity Too Large";
            case (414):
                return "Request-URI Too Long";
            case (415):
                return "Unsupported Media Type";
            case (416):
                return "Requested Range Not Satisfiable";
            case (417):
                return "Expectation Failed";
            case (418):
                return "Reauthentication Required";
            case (419):
                return "Proxy Reauthentication Required";
            case (422):
                return "Unprocessable Entity";
            case (423):
                return "Locked";
            case (424):
                return "Failed Dependency";
            case (500):
                return "Server Error";
            case (501):
                return "Not Implemented";
            case (502):
                return "Bad Gateway";
            case (503):
                return "Service Unavailable";
            case (504):
                return "Gateway Timeout";
            case (505):
                return "HTTP Version Not Supported";
            case (507):
                return "Insufficient Storage";
            default:
                return "";
        }
    }

    /**
     * It returns a map of all the headers in the response
     *
     * @return A map of the headers.
     */
    public Map<String, List<String>> getHeaders() {
        return httpResponse.headers().map();
    }

    /**
     * > Returns the body of the response as a string
     *
     * @return The body of the response.
     */
    public String body() {
        return httpResponse.body();
    }

    /**
     * Return a stream of the body as a byte array.
     *
     * @return A stream of bytes that represent the body of the response.
     */
    public InputStream bodyStream() {
        return new ByteArrayInputStream(body().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Return the body of the response as an XML document.
     *
     * @return A Document object
     */
    public Document bodyXml() {
        return XmlMapper.parse(bodyStream());
    }

    /**
     * It takes the body of the HTTP response, converts it to a string, and then
     * converts that string to a JSON object
     *
     * @return A JsonNode object
     */
    public JsonNode bodyJson() {
        return JsonUtils.toJson(httpResponse.body());
    }

    /**
     * "If the response body is a JSON string, parse it into an object of the
     * given type."
     * <p>
     * The function is generic, so it can be used to parse the response body
     * into any type of object
     *
     * @param  responseType The type of the response body.
     * @return              The body of the response as a string.
     */
    public <T> T bodyAs(final Class<T> responseType) {
        return DataMapper.parse(body(), responseType);
    }

    /**
     * > Returns the status code of the HTTP response
     *
     * @return The status code of the response.
     */
    public int statusCode() {
        return httpResponse.statusCode();
    }

    /**
     * If the header exists, return it, otherwise return an empty string.
     *
     * @param  name The name of the header to retrieve.
     * @return      The value of the header with the given name.
     */
    public String header(final String name) {
        Optional<String> token = httpResponse.headers().firstValue(name);
        return token.orElse("");
    }

    private void logIfError() {
        int responseCode = statusCode();
        if (responseCode >= 400) {
            throw new SelcukesException(
                String.format("Received an unsuccessful HTTP response with status code [%d] [%s]", responseCode,
                    getReasonPhrase(responseCode)));
        }
    }
}
