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

package io.github.selcukes.commons.exception;

import io.github.selcukes.commons.helper.ExceptionHelper;
import io.github.selcukes.databind.utils.StringHelper;

import java.util.function.Supplier;

public class BusinessException extends SelcukesException {
    private String code;

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.setCode(cause.getClass().getSimpleName());
    }

    public String getCode() {
        return ExceptionHelper.getErrorCodes().getMessage(StringHelper.toSnakeCase(code));
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Supplier<String> logError() {
        return () -> this.getMessage() + "\nHow to fix: \n" + this.getCode();
    }
}
