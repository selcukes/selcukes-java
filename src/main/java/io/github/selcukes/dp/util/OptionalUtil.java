package io.github.selcukes.dp.util;

import java.util.Optional;

public class OptionalUtil {
    public static <T> T unwrap(Optional<T> optional) {
        return optional.orElse(null);
    }
}
