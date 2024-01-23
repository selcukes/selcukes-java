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
// @Getter
public record WebResponse(HttpResponse<String> httpResponse) {

    /**
     * It returns the reason phrase for a given HTTP status code
     *
     * @param  statusCode The HTTP status code.
     * @return            The reason phrase for the given status code.
     */
    @SuppressWarnings("java:S1479")
    public static String getReasonPhrase(final int statusCode) {
        return switch (statusCode) {
            case (200) -> "OK";
            case (201) -> "Created";
            case (202) -> "Accepted";
            case (203) -> "Non Authoritative Information";
            case (204) -> "No Content";
            case (205) -> "Reset Content";
            case (206) -> "Partial Content";
            case (207) -> "Partial Update OK";
            case (300) -> "Mutliple Choices";
            case (301) -> "Moved Permanently";
            case (302) -> "Moved Temporarily";
            case (303) -> "See Other";
            case (304) -> "Not Modified";
            case (305) -> "Use Proxy";
            case (307) -> "Temporary Redirect";
            case (400) -> "Bad Request";
            case (401) -> "Unauthorized";
            case (402) -> "Payment Required";
            case (403) -> "Forbidden";
            case (404) -> "Not Found";
            case (405) -> "Method Not Allowed";
            case (406) -> "Not Acceptable";
            case (407) -> "Proxy Authentication Required";
            case (408) -> "Request Timeout";
            case (409) -> "Conflict";
            case (410) -> "Gone";
            case (411) -> "Length Required";
            case (412) -> "Precondition Failed";
            case (413) -> "Request Entity Too Large";
            case (414) -> "Request-URI Too Long";
            case (415) -> "Unsupported Media Type";
            case (416) -> "Requested Range Not Satisfiable";
            case (417) -> "Expectation Failed";
            case (418) -> "Reauthentication Required";
            case (419) -> "Proxy Reauthentication Required";
            case (422) -> "Unprocessable Entity";
            case (423) -> "Locked";
            case (424) -> "Failed Dependency";
            case (500) -> "Server Error";
            case (501) -> "Not Implemented";
            case (502) -> "Bad Gateway";
            case (503) -> "Service Unavailable";
            case (504) -> "Gateway Timeout";
            case (505) -> "HTTP Version Not Supported";
            case (507) -> "Insufficient Storage";
            default -> "";
        };
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

    void logIfError() {
        int responseCode = statusCode();
        if (responseCode >= 400) {
            throw new SelcukesException(
                String.format("Received an unsuccessful HTTP response with status code [%d] [%s]", responseCode,
                    getReasonPhrase(responseCode)));
        }
    }
}
