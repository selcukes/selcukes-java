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

import io.github.selcukes.collections.Resources;
import io.github.selcukes.commons.http.WebClient;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class WebClientTest {

    @Test(enabled = false)
    public void postTest() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\":\"Ramesh\",");
        json.append("\"notes\":\"hello\"");
        json.append("}");

        var client = new WebClient("http://postman-echo.com/post");
        var response = client.post(json);
        assertTrue(response.body().contains("Ramesh"));
    }

    @Test(enabled = false)
    public void uploadFileTest() {
        var file = Resources.ofTest("sample.csv");
        var client = new WebClient("http://postman-echo.com/post");
        var param = Map.of("files", file);
        var responseBody = client.multiPart(param).post().bodyJson();
        assertTrue(responseBody.at("/files").has("sample.csv"));
    }

    @Test
    public void requestTest() {
        var client = new WebClient("https://reqres.in/api/users/2");
        var responseBody = client.get().bodyJson();
        assertEquals(responseBody.at("/data/id").asText(), "2");
        assertEquals(responseBody.at("/data/first_name").asText(), "Janet");
    }

    @Test(enabled = false)
    public void bearerAuthTest() {
        var client = new WebClient("https://httpbin.org/#/Auth/get_bearer");
        var response = client.authenticator("hello")
                .get();
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void authTest() {
        var client = new WebClient("https://postman-echo.com/basic-auth");
        var responseBody = client.authenticator("postman", "password")
                .get().bodyJson();
        assertTrue(responseBody.at("/authenticated").asBoolean());
    }
}
