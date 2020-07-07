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
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import static org.apache.hc.client5.http.config.RequestConfig.custom;
import static org.apache.hc.client5.http.cookie.StandardCookieSpec.STRICT;

class HttpClient {
    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final String APPLICATION_JSON = "application/json";
    private static final String HTTPS = "https";
    private CloseableHttpClient client;


    public HttpGet createHttpGet(URL url) {
        HttpGet httpGet = new HttpGet(url.toString());
        httpGet.addHeader(HttpHeaders.USER_AGENT, "Apache-HttpClient/5.0");
        httpGet.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
        httpGet.addHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");

        RequestConfig requestConfig = custom().setCookieSpec(STRICT)
            .setConnectTimeout(2, TimeUnit.SECONDS)
            .build();
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    private void setConnection(HttpClientBuilder builder) throws GeneralSecurityException {
        HostnameVerifier allHostsValid = (hostname, session) -> hostname
            .equalsIgnoreCase(session.getPeerHost());
        SSLContext sslContext = SSLContexts.custom()
            .loadTrustMaterial(null, (chain, authType) -> true).build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
            sslContext, allHostsValid);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
            .<ConnectionSocketFactory>create().register(HTTPS, socketFactory)
            .register(HTTPS, new PlainConnectionSocketFactory())
            .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
            socketFactoryRegistry);
        builder.setConnectionManager(cm);
    }

    protected HttpClient createClient() {
        HttpClientBuilder builder = HttpClientBuilder.create()
            .setConnectionManagerShared(true);
        try {
            setConnection(builder);
        } catch (GeneralSecurityException e) {
            throw new SelcukesException(e);
        }
        client = builder.useSystemProperties().build();
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
