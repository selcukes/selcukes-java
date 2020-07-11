/*
 *
 *  * Copyright (c) Ramesh Babu Prudhvi.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

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
