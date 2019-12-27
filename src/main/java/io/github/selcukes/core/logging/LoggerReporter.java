package io.github.selcukes.core.logging;

import java.io.IOException;
import java.io.InputStream;
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
    public static void loadLoggerConfig()
    {
        InputStream stream = LoggerReporter.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);

        } catch (IOException e) {
            logger.severe(e::getMessage);
        }
    }
}
