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

package io.github.selcukes.core.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.core.exception.SelcukesException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

class HttpClient {
    private static final String APPLICATION_JSON = "application/json";
    private CloseableHttpClient client;

    protected HttpGet createHttpGet(String url) {
        return new HttpGet(url);
    }

    protected HttpClient createClient() {
        HttpClientBuilder builder = HttpClientBuilder.create().disableRedirectHandling();
        client = builder.build();
        return this;
    }

    protected CloseableHttpResponse execute(ClassicHttpRequest request) {
        try {
            return client.execute(request);
        } catch (IOException e) {
            throw new SelcukesException(e);
        }
    }

    protected HttpEntity createStringEntity(Object payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String message = mapper.writeValueAsString(payload);
            return new StringEntity(message);
        } catch (JsonProcessingException e) {
            throw new SelcukesException(e);
        }
    }

    protected HttpEntity createMultipartEntityBuilder(FileBody fileBody) {
        return MultipartEntityBuilder.create().addPart("file", fileBody).build();
    }

    protected HttpPost createHttpPost(String url, HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
        return httpPost;
    }

    protected CloseableHttpResponse execute(HttpPost post) {
        try {
            return client.execute(post);
        } catch (IOException e) {
            throw new SelcukesException(e);
        }
    }
}
