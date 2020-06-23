/*
 *
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
 *
 */

package io.github.selcukes.reports.video;

import io.github.selcukes.reports.enums.RecorderType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class RecorderFactory {
    public static synchronized Recorder getRecorder(RecorderType recorderType) {
        Map<RecorderType, Supplier<Recorder>> recorderFactory = new EnumMap<RecorderType, Supplier<Recorder>>(RecorderType.class) {{
            put(RecorderType.MONTE, MonteRecorder::new);
            put(RecorderType.FFMPEG, FFmpegRecorder::new);

        }};
        return recorderFactory.get(recorderType).get();
    }
}
