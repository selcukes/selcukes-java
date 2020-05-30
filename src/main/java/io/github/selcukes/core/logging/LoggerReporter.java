/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.logging;

import java.util.logging.Logger;
import java.util.logging.*;

public class LoggerReporter {
    private static final Logger logger = Logger.getLogger(LoggerReporter.class.getName());

    private LoggerReporter() {

    }

    public static void setLoggerFormat(Level newLevel) {
        LogManager.getLogManager().reset();
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        SelcukesLoggerFormatter formatter = new SelcukesLoggerFormatter();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(newLevel);
        handler.setFormatter(formatter);
        rootLogger.addHandler(handler);
        rootLogger.setLevel(newLevel);
        try {
            FileHandler fileTxt = new FileHandler("application.log");
            fileTxt.setFormatter(formatter);
            rootLogger.addHandler(fileTxt);
        } catch (Exception e) {
            logger.severe(e::getMessage);
        }
    }

}
