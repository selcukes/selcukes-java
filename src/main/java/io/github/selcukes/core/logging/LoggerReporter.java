package io.github.selcukes.core.logging;

import java.util.logging.Logger;
import java.util.logging.*;

public class LoggerReporter {
    private static final Logger logger = Logger.getLogger(LoggerReporter.class.getName());

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
