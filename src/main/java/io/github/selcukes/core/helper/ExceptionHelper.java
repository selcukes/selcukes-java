package io.github.selcukes.core.helper;


import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;

public class ExceptionHelper {
    private ExceptionHelper()
    {

    }
    private static Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

    public static <T> T rethrow(Exception e) {
        logger.error(() -> "Rethrow exception: " + e.getClass().getName() + e.getMessage());
        throw new IllegalStateException(e);
    }
}