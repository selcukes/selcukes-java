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

package io.github.selcukes.video;

import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.video.config.VideoConfig;


public abstract class VideoRecorder implements Recorder {

    public static VideoConfig conf() {
        VideoConfig config = VideoConfig.builder()
            .build();
        if (Platform.isLinux()) {
            config.setFfmpegFormat("x11grab");
            config.setFfmpegDisplay(":0.0");
        }
        return config;
    }
}
