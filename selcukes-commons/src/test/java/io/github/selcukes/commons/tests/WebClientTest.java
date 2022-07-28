/*
 *
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.selcukes.commons.tests;

import io.github.selcukes.commons.http.Response;
import io.github.selcukes.commons.http.WebClient;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import lombok.SneakyThrows;
import org.testng.annotations.Test;

public class WebClientTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @SneakyThrows
    @Test
    public void postTest() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\":\"Ramesh\",");
        json.append("\"notes\":\"hello\"");
        json.append("}");

        WebClient client = new WebClient("https://httpbin.org/post");
        Response response = client.post(json);

        logger.info(response::body);
    }

    @SneakyThrows
    @Test
    public void requestTest() {

        WebClient client = new WebClient("https://httpbin.org/get");
        Response response = client.get();
        logger.info(response::body);
    }

    @SneakyThrows
    @Test
    public void bearerAuthTest() {

        WebClient client = new WebClient("https://httpbin.org/#/Auth/get_bearer");
        Response response = client.authenticator("hello")
                .get();
        logger.debug(response::body);
    }

    @SneakyThrows
    @Test
    public void authTest() {

        WebClient client = new WebClient("https://httpbin.org/#/Auth/get_basic_auth__user___passwd_");
        Response response = client.authenticator("hello", "hello")
                .get();
        logger.debug(() -> response.statusCode() + "");
    }
}
