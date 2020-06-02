/*
 *
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
 *
 */

package io.github.selcukes.core.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.core.exception.SelcukesException;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Optional;

public class HttpClient implements Closeable {
    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final String APPLICATION_JSON = "application/json";
    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper;
    private HttpEntity httpEntity;
    private final String webHookUrl;
    private String proxy;

    public HttpClient(String webHookUrl) {
        this.webHookUrl = webHookUrl;
        this.httpClient = createDefaultHttpClient();
        this.mapper = new ObjectMapper();
    }
    
    public HttpClient(String url, String proxy) {
        this.webHookUrl = url;
        this.proxy = proxy;
        this.httpClient = createHttpClient();
        this.mapper = new ObjectMapper();
    }

    private CloseableHttpClient createDefaultHttpClient() {
        return HttpClients.createDefault();
    }

    public String post(Object payload) {
        String message = null;
        try {
            message = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new SelcukesException(e);
        }
        return execute(httpClient, webHookUrl, createHttpEntity(message));
    }

    private HttpEntity createHttpEntity(String message) {
        try {
            this.httpEntity = new StringEntity(message);
        } catch (UnsupportedEncodingException e) {
            throw new SelcukesException(e);
        }
        return this.httpEntity;
    }

    public String post(FileBody fileBody) {
        return execute(httpClient, webHookUrl, createMultipartEntityBuilder(fileBody));
    }

    private HttpEntity createMultipartEntityBuilder(FileBody fileBody) {
        this.httpEntity = MultipartEntityBuilder.create().addPart("file", fileBody).build();
        return this.httpEntity;
    }

    private String execute(CloseableHttpClient httpClient, String url, HttpEntity httpEntity) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(httpEntity);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);

            String retStr = httpClient.execute(httpPost, new StringResponseHandler());

            logger.warn(() -> "return : " + retStr);

            return retStr;
        } catch (IOException e) {
            throw new SelcukesException(e);
        }

    }

    private HttpClientBuilder createHttpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().disableRedirectHandling();

        return isProxy().isPresent() ? httpClientBuilder.setProxy(getProxyHost()) : httpClientBuilder;
    }

    private HttpHost getProxyHost() {
        return new HttpHost(proxy);
    }

    private CloseableHttpClient createHttpClient() {
        return createHttpClientBuilder().build();
    }

    private HttpUriRequest createHttpRequest() {
        try {
            URI uri = new URI(webHookUrl);
            return RequestBuilder.get()
                .setUri(uri)
                .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .build();
        } catch (URISyntaxException e) {
            throw new SelcukesException(e);
        }
    }

    private CloseableHttpResponse getHttpResponse() {
        try {
            return httpClient.execute(createHttpRequest());
        } catch (IOException e) {
            throw new SelcukesException(e);
        }

    }


    public String getHeaderValue(String headerName) {
        return getHttpResponse().getFirstHeader(headerName).getValue();
    }

    public InputStream getResponseStream() {
        InputStream inputStream;
        try {
            this.httpEntity = getHttpResponse().getEntity();
            inputStream = httpEntity.getContent();
        } catch (IOException e) {
            throw new SelcukesException(e);
        }
        return inputStream;
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

    @Override
    public void close() throws IOException {
        this.httpClient.close();
    }
}
