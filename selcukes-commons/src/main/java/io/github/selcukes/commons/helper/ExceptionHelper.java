/*
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
 */

package io.github.selcukes.commons.helper;


import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.resource.DataMapper;
import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ExceptionHelper {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);
    private static ErrorCodes errorCodes;

    public <T> T rethrow(Exception e) {
        logger.error(() -> "Rethrow exception: " + e.getClass().getName() + e.getMessage());
        throw new IllegalStateException(e);
    }

    public String getStackTrace(Throwable throwable) {
        if (throwable == null)
            return null;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public String getExceptionTitle(Throwable throwable) {
        Pattern pattern = Pattern.compile("([\\w\\.]+)(:.*)?");
        String stackTrace = getStackTrace(throwable);
        Matcher matcher = pattern.matcher(stackTrace);

        if (matcher.find())
            return matcher.group(1);

        return null;
    }

    public ErrorCodes getErrorCodes() {
        if (errorCodes == null) {
            errorCodes = DataMapper.parse(ErrorCodes.class);
        }
        return errorCodes;
    }
}