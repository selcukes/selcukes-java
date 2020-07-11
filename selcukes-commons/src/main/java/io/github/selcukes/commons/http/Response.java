/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.http;

import io.github.selcukes.commons.exception.SelcukesException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ProtocolException;

import java.io.IOException;
import java.io.InputStream;

public class Response {

    private final CloseableHttpResponse httpResponse;

    public Response(CloseableHttpResponse httpResponse) {

        this.httpResponse = httpResponse;
        logIfError();
    }

    public String getHeader(String name) {
        try {
            return httpResponse.getHeader(name).toString();
        } catch (ProtocolException e) {
            throw new SelcukesException(e);
        }
    }

    public InputStream getResponseStream() {
        try {
            return httpResponse.getEntity().getContent();
        } catch (IOException e) {
            throw new SelcukesException(e);
        }
    }

    public void logIfError() {
        int responseCode = httpResponse.getCode();
        if (responseCode >= HttpStatus.SC_BAD_REQUEST) {
            throw new SelcukesException(String.format("HttpResponseException : Response Code[%s] Reason Phrase[%s]",
                responseCode, httpResponse.getReasonPhrase()));
        }
    }
}
