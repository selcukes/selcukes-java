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
import io.github.selcukes.databind.utils.StringHelper;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Response {
    @Getter
    private final HttpResponse<String> httpResponse;

    public Response(HttpResponse<String> httpResponse) {
        this.httpResponse = httpResponse;
        logIfError();
    }

    public Map<String, List<String>> getHeaders() {
        return httpResponse.headers().map();
    }

    public String getBody() {
        return httpResponse.body();
    }

    public InputStream bodyStream() {
        return new ByteArrayInputStream(getBody().getBytes(StandardCharsets.UTF_8));
    }

    public JsonNode getBodyAsJson() {
        return StringHelper.toJson(httpResponse.body());
    }

    public <T> T getBodyAs(Class<T> responseType) {
        return DataMapper.parse(getBody(), responseType);
    }

    public int getStatusCode() {
        return httpResponse.statusCode();
    }

    public String getHeader(String name) {
        Optional<String> token = httpResponse.headers().firstValue(name);
        return token.orElse("");
    }

    public void logIfError() {
        int responseCode = getStatusCode();
        if (responseCode >= 400) {
            throw new SelcukesException(String.format("HttpResponseException : Response Code[%s] Reason Phrase[%s]",
                responseCode, getReasonPhrase(responseCode)));
        }
    }

    public static String getReasonPhrase(int statusCode) {
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
}
