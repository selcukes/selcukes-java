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

import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.video.enums.RecorderType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RecorderFactory {
    /**
     * If the recorder type is FFMPEG, return a new FFmpegRecorder, otherwise
     * return a new MonteRecorder.
     *
     * @param  recorderType The type of recorder to use.
     * @return              A new instance of FFmpegRecorder or MonteRecorder.
     */
    public synchronized Recorder getRecorder(final RecorderType recorderType) {
        return recorderType.equals(RecorderType.FFMPEG) ? new FFmpegRecorder() : new MonteRecorder();
    }

    /**
     * If the video type is FFMPEG, return a new FFmpegRecorder, otherwise
     * return a new MonteRecorder.
     *
     * @return A new instance of a Recorder.
     */
    public synchronized Recorder getRecorder() {
        return ConfigFactory.getConfig()
                .getVideo().getType()
                .equalsIgnoreCase("FFMPEG") ? new FFmpegRecorder() : new MonteRecorder();
    }
}
