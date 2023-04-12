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

import io.github.selcukes.commons.http.WebResponse;
import lombok.CustomLog;

import static io.github.selcukes.commons.http.WebResponse.getReasonPhrase;
import static io.github.selcukes.core.validation.Validation.failWithMessage;

@CustomLog
public class ResponseValidation {
    private final WebResponse response;
    private final boolean isSoft;
    private final String responseBody;

    public ResponseValidation(boolean isSoft, WebResponse response) {
        this.response = response;
        responseBody = response.body();
        this.isSoft = isSoft;
    }

    public ResponseValidation isOk() {
        int statusCode = response.statusCode();
        boolean isOk = statusCode == 0 || (statusCode >= 200 && statusCode <= 299);
        if (!isOk) {
            failWithMessage(isSoft, "Expected Response Status should be [%s] but was [%s]", "OK",
                getReasonPhrase(statusCode));
        }
        return this;
    }

    public ResponseValidation containsText(String expectedText) {
        logger.info(() -> String.format("Verifying Response should contains text [%s]", expectedText));
        if (!responseBody.contains(expectedText)) {
            failWithMessage(isSoft, "Expected Response should contain text [%s] but actual response was [%s]",
                expectedText, responseBody);
        }
        return this;
    }

}
