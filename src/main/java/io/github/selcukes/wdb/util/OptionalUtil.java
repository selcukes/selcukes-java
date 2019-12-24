package io.github.selcukes.wdb.util;

import java.util.Optional;
import java.util.function.Supplier;

public class OptionalUtil {
    private OptionalUtil() {

    }

    public static <T> T unwrap(Optional<T> optional) {
        return optional.orElse(null);
    }

    public static <T> Optional<T> orElse(Optional<T> optional, T fallbackValue) {

        if (optional.isPresent())
            return optional;
        else
            return Optional.of(fallbackValue);
    }
    public static String orElse(Optional<String> optional, String fallbackValue) {

        return optional.orElse(fallbackValue);
    }

    public static <T> Optional<T> unwrap(Optional<T> optional, Supplier<T> fallbackValue) {
        if (optional.isPresent()) {
            return optional;
        }

        return Optional.ofNullable(fallbackValue.get());
    }


}
