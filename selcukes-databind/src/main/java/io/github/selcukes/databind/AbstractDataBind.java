/*
 *
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.github.selcukes.databind;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;

abstract class AbstractDataBind implements DataBind {
    private final ObjectMapper mapper;

    AbstractDataBind(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SneakyThrows
    @Override
    public <T> T parse(final String path, final Class<T> resourceClass) {
        return this.mapper.readValue(new File(path), resourceClass);
    }

    @SneakyThrows
    @Override
    public <T> void write(final String path, final T value) {
        this.mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), value);
    }
}
