/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.video.config;

import io.github.selcukes.commons.config.Environment;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.Dimension;
import java.awt.Toolkit;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class DefaultVideoOptions extends Environment {
    @Builder.Default
    String videoFolder = "video-report";
    @Builder.Default
    int frameRate = 24;
    @Builder.Default
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    @Builder.Default
    String ffmpegFormat = "gdigrab";
    @Builder.Default
    String ffmpegDisplay = "desktop";
    @Builder.Default
    String pixelFormat = "yuv420p";
}
