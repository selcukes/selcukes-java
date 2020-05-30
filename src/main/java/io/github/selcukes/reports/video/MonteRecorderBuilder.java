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

import io.github.selcukes.core.exception.RecorderException;
import org.monte.media.Format;

import java.awt.*;
import java.io.File;
import java.io.IOException;

class MonteRecorderBuilder {

    private GraphicsConfiguration cfg;
    private Format fileFormat;
    private Format screenFormat;
    private Format mouseFormat;
    private Format audioFormat;
    private File folder;
    private Rectangle rectangle;
    private String fileName;

    public static Builder builder() {
        return new MonteRecorderBuilder().new Builder();
    }

    public class Builder {

        public Builder setGraphicConfig(GraphicsConfiguration cfg) {
            MonteRecorderBuilder.this.cfg = cfg;
            return this;
        }

        public Builder setFileFormat(Format fileFormat) {
            MonteRecorderBuilder.this.fileFormat = fileFormat;
            return this;
        }

        public Builder setScreenFormat(Format screenFormat) {
            MonteRecorderBuilder.this.screenFormat = screenFormat;
            return this;
        }

        public Builder setMouseFormat(Format mouseFormat) {
            MonteRecorderBuilder.this.mouseFormat = mouseFormat;
            return this;
        }

        public Builder setAudioFormat(Format audioFormat) {
            MonteRecorderBuilder.this.audioFormat = audioFormat;
            return this;
        }

        public Builder setFolder(File folder) {
            MonteRecorderBuilder.this.folder = folder;
            return this;
        }

        public Builder setFileName(String fileName) {
            MonteRecorderBuilder.this.fileName = fileName;
            return this;
        }

        public Builder setRectangle(Rectangle rectangle) {
            MonteRecorderBuilder.this.rectangle = rectangle;
            return this;
        }

        public MonteRecorder build() {
            try {
                return new MonteRecorder(cfg,
                        rectangle,
                        fileFormat,
                        screenFormat,
                        mouseFormat,
                        audioFormat,
                        folder);
            } catch (IOException | AWTException e) {
                throw new RecorderException(e);
            }
        }

    }
}
