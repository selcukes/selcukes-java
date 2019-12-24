package io.github.selcukes.wdb.util;

import java.util.Optional;

public class OptionalUtil {
    private OptionalUtil() {

    }

    public static <T> T unwrap(Optional<T> optional) {
        return optional.orElse(null);
    }

    public static <T> T orElse(Optional<T> optional, T fallbackValue) {

        return optional.orElse(fallbackValue);
    }

}
