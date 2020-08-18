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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.commons.exception.SelcukesException;
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
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Optional;

class HttpClient {
    private static final String APPLICATION_JSON = "application/json";
    private CloseableHttpClient client;
    private String proxy;

    public HttpClient(String proxy) {
        this.proxy = proxy;
    }

    public HttpClient() {

    }

    protected HttpGet createHttpGet(String url) {
        return new HttpGet(url);
    }

    protected HttpClient createClient() {
        HttpClientBuilder builder = HttpClientBuilder.create().disableRedirectHandling();
        if (isProxy().isPresent()) builder.setProxy(getProxyHost());
        client = builder.build();
        return this;
    }

    private HttpHost getProxyHost() {
        return new HttpHost(proxy);
    }

    private Optional<Proxy> isProxy() {
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

    private Optional<URL> getProxyUrl(String proxy) {
        try {
            return Optional.of(new URL(proxy));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
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

    protected HttpEntity createMultipartEntity(FileBody fileBody) {
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
