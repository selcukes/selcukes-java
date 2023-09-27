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

package io.github.selcukes.core.driver;

import io.github.selcukes.commons.config.ConfigFactory;
import lombok.experimental.UtilityClass;

import static io.github.selcukes.collections.StringHelper.isNonEmpty;

@UtilityClass
public class RunMode {
    static boolean isCloudAppium() {
        String cloud = ConfigFactory.getConfig().getMobile().getCloud();
        return isNonEmpty(cloud);
    }

    static boolean isLocalAppium() {
        return !ConfigFactory.getConfig().getMobile().isRemote();
    }

    static boolean isLocalBrowser() {
        return !ConfigFactory.getConfig().getWeb().isRemote();
    }

    static boolean isCloudBrowser() {
        String cloud = ConfigFactory.getConfig().getWeb().getCloud();
        return isNonEmpty(cloud);
    }

    boolean isHeadlessWeb() {
        return ConfigFactory.getConfig().getWeb().isHeadLess();
    }

    boolean isHeadlessMobile() {
        return ConfigFactory.getConfig().getMobile().isHeadLess();
    }
}
