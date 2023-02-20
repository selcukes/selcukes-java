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

import io.github.selcukes.commons.exception.BusinessException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.databind.DataMapper;
import io.github.selcukes.databind.utils.StringHelper;
import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.util.Optional.ofNullable;

@UtilityClass
public class ExceptionHelper {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);
    private static ErrorCodes errorCodes;

    /**
     * If you call this function, you're going to get an exception.
     *
     * @param  e The exception to be rethrown.
     * @return   Nothing.
     */
    public <T> T rethrow(final Exception e) {
        logger.error(() -> "Rethrow exception: " + e.getClass().getName() + e.getMessage());
        throw new IllegalStateException(e);
    }

    /**
     * It creates a StringWriter, which is a character stream that collects its
     * output in a string buffer, which can then be used to construct a string
     *
     * @param  throwable The throwable to get the stack trace from.
     * @return           A string representation of the stack trace of the
     *                   throwable.
     */
    public String getStackTrace(Throwable throwable) {
        return ofNullable(throwable)
                .map(ex -> {
                    var stringWriter = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stringWriter));
                    return stringWriter.toString();
                })
                .orElse(null);
    }

    /**
     * > It returns the first word of the first line of the stack trace of the
     * given throwable
     *
     * @param  throwable The exception that was thrown.
     * @return           The title of the exception.
     */
    public String getExceptionTitle(final Throwable throwable) {
        return StringHelper.findPattern("([\\w.]+)(:.*)?", getStackTrace(throwable))
                .orElse(null);
    }

    /**
     * If the errorCodes object is null, then parse the ErrorCodes.json file and
     * store the result in the errorCodes object
     *
     * @return The errorCodes object.
     */
    public static ErrorCodes getErrorCodes() {
        if (errorCodes == null) {
            errorCodes = DataMapper.parse(ErrorCodes.class);
        }
        return errorCodes;
    }

    /**
     * > This function takes a throwable, creates a BusinessException with the
     * throwable's message and the throwable itself, and then logs the exception
     *
     * @param throwable The exception that was thrown.
     */
    public static void handleException(final Throwable throwable) {
        BusinessException exception = new BusinessException(throwable.getMessage(), throwable);
        logger.error(throwable, exception.logError());
    }
}
