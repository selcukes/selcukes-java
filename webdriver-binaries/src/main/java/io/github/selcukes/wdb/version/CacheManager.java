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

package io.github.selcukes.wdb.version;

import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.databind.properties.PropertiesMapper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class CacheManager {
    private static Path versionPath = Path.of("");

    private CacheManager() {
        // Do nothing
    }

    public static void setTargetPath(String targetPath) {
        versionPath = Paths.get(targetPath, "version.properties");
    }

    static Optional<String> resolveVersion(String key) {
        if (versionPath.toFile().exists()) {
            Map<String, String> props = PropertiesMapper.parse(versionPath);
            String timestamp = props.get("timestamp");
            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
            if (dateTime.isBefore(LocalDateTime.now())) {
                FileHelper.deleteFile(versionPath.toFile());
                return Optional.empty();
            } else {
                return Optional.ofNullable(props.get(key));
            }
        }
        return Optional.empty();
    }

    static void createCache(String key, String value) {
        if (!versionPath.toFile().exists()) {
            Map<String, String> data = new LinkedHashMap<>();
            data.put("chromedriver", "");
            data.put("msedgedriver", "");
            data.put("timestamp", String.valueOf(LocalDateTime.now().plusHours(1)));
            PropertiesMapper.write(versionPath, data);
        }
        PropertiesMapper.updateProperty(versionPath, key, value);
    }
}
