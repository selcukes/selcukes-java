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

package io.github.selcukes.core.validation;

import io.github.selcukes.commons.http.Response;
import io.github.selcukes.core.page.Page;

import static io.github.selcukes.commons.http.Response.getReasonPhrase;
import static io.github.selcukes.core.validation.Validation.failWithMessage;

public class ResponseValidation {
    Response response;
    Page page;

    boolean isSoft;

    public ResponseValidation(boolean isSoft, Page page, Response response) {
        this.response = response;
        this.page = page;
        this.isSoft = isSoft;
    }

    public ResponseValidation isOK() {
        if (response.getStatusCode() != 200) {
            failWithMessage(isSoft, "Expected Response Status should be [%s] but was [%s]", "OK", getReasonPhrase(response.getStatusCode()));
        }
        return this;
    }

    public ResponseValidation containsText(String expectedText) {
        if (!response.getBody().contains(expectedText)) {
            failWithMessage(isSoft, "Expected Response should contain text [%s] but actual text was [%s]", expectedText, response.getBody());
        }
        return this;
    }

}
