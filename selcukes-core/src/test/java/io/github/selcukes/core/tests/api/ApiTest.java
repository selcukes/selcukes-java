/*
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
 */

package io.github.selcukes.core.tests.api;

import io.github.selcukes.commons.http.Response;
import io.github.selcukes.core.page.ApiPage;
import io.github.selcukes.core.page.Pages;
import lombok.CustomLog;
import lombok.Data;
import org.testng.annotations.Test;

@CustomLog
public class ApiTest {
    @Test
    public void authTest() {
        String user = "{\n" +
            "    \"email\": \"eve.holt@reqres.in\",\n" +
            "    \"password\": \"admin\"\n" +
            "}";
        ApiPage page = Pages.apiPage();
        Response response = page.request("https://reqres.in/api/register")
            .post(user);
        page.assertThat().response(response).isOk();
        logger.info(() -> "Token is: " + response.bodyAs(ResponseBody.class).getToken());
    }

    @Data
    static class ResponseBody {
        String id;
        String token;
    }
}
