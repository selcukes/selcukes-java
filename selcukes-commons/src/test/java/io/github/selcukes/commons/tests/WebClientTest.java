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

import io.github.selcukes.commons.http.WebClient;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.databind.utils.Resources;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertTrue;

public class WebClientTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void postTest() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\":\"Ramesh\",");
        json.append("\"notes\":\"hello\"");
        json.append("}");

        var client = new WebClient("https://httpbin.org/post");
        var response = client.post(json);
        assertTrue(response.body().contains("Ramesh"));
    }

    @Test
    public void uploadFileTest() {
        var file = Resources.ofTest("sample.csv");
        var client = new WebClient("http://postman-echo.com/post");
        Map<Object, Object> param = Map.of("files", file);
        var response = client.multiPart(param).post();
        assertTrue(response.bodyJson().at("/files").has("sample.csv"));
    }

    @Test
    public void requestTest() {
        var client = new WebClient("https://httpbin.org/get");
        var response = client.get();
        logger.info(response::body);
    }

    @Test
    public void bearerAuthTest() {
        var client = new WebClient("https://httpbin.org/#/Auth/get_bearer");
        var response = client.authenticator("hello")
                .get();
        logger.debug(response::body);
    }

    @Test
    public void authTest() {
        var client = new WebClient("https://postman-echo.com/basic-auth");
        var responseBody = client.authenticator("postman", "password")
                .get().bodyJson();
        assertTrue(responseBody.at("/authenticated").asBoolean());
    }
}
