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

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;

public class WebClient {
    private final HttpClient client;
    private final String url;

    public WebClient(String url) {
        this.url = url;
        this.client = new HttpClient();
    }

    public WebClient(String url, String proxy) {
        this.url = url;
        this.client = new HttpClient(proxy);
    }

    public Response sendRequest() {
        ClassicHttpRequest request = client.createHttpGet(url);
        return new Response(client.createClient().execute(request));
    }

    public <T> Response post(T payload) {
        HttpEntity httpEntity = (payload instanceof FileBody) ?
            client.createMultipartEntity((FileBody) payload) : client.createStringEntity(payload);
        HttpPost post = client.createHttpPost(url, httpEntity);
        return new Response(client.createClient().execute(post));
    }

}
