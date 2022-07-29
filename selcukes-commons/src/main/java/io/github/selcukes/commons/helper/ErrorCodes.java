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

package io.github.selcukes.commons.helper;

import io.github.selcukes.databind.annotation.DataFile;
import lombok.Data;

import java.util.Map;

/**
 * > This class contains a list of error codes that can be returned by the API
 */
@Data
@DataFile(fileName = "error_codes.yaml", streamLoader = true)
public class ErrorCodes {
    private Map<String, String> errors;

    /**
     * If the code is found in the errors map, return the value associated with that key. Otherwise, return the default
     * value
     *
     * @param code The error code that you want to get the message for.
     * @return The error message associated with the error code.
     */
    public String getMessage(final String code) {
        return this.errors.getOrDefault(code, "Solution not found in error_code.yaml file");
    }
}
