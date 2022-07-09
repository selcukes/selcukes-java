/*
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
 */

package io.github.selcukes.databind.tests;

import io.github.selcukes.databind.substitute.DefaultSubstitutor;
import io.github.selcukes.databind.utils.StringHelper;

import java.util.Properties;

public class EnvPropSubstitutor extends DefaultSubstitutor {
    @Override
    public String replace(Properties variables, String key) {
        String value = variables.getProperty(key);
        return StringHelper.interpolate(value,
            matcher -> System.getenv(matcher.group(1)));
    }
}
