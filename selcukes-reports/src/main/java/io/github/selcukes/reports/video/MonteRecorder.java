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

package io.github.selcukes.reports.video;

import io.github.selcukes.commons.exception.RecorderException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.reports.config.VideoConfig;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.monte.media.VideoFormatKeys.*;

class MonteRecorder extends VideoRecorder {
    private final Logger logger = LoggerFactory.getLogger(MonteRecorder.class);
    private final MonteRecorderBuilder recorderBuilder;
    private final VideoConfig videoConfig;

    public MonteRecorder() {
        this.videoConfig = conf();
        this.recorderBuilder = getScreenRecorder();
    }

    @Override
    public void start() {
        recorderBuilder.start();
        logger.info(() -> "Recording started");
    }

    @Override
    public File stopAndSave(String filename) {
        File video = writeVideo(filename);
        logger.info(() -> "Recording finished to " + video.getAbsolutePath());
        return video;
    }

    @Override
    public void stopAndDelete() {
        File video = writeVideo("Temp");
        logger.info(() -> "Deleting recorded video file...");
        video.deleteOnExit();
    }

    private File writeVideo(String filename) {
        try {
            return recorderBuilder.saveAs(filename);
        } catch (IndexOutOfBoundsException ex) {
            throw new RecorderException("Video recording wasn't started");
        }
    }

    private GraphicsConfiguration getGraphicConfig() {
        return GraphicsEnvironment
            .getLocalGraphicsEnvironment().getDefaultScreenDevice()
            .getDefaultConfiguration();
    }

    private MonteRecorderBuilder getScreenRecorder() {
        int frameRate = videoConfig.getFrameRate();

        Format fileFormat = new Format(MediaTypeKey, MediaType.VIDEO, MimeTypeKey, FormatKeys.MIME_AVI);
        Format screenFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,
            ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
            DepthKey, 24, FrameRateKey, Rational.valueOf(frameRate),
            QualityKey, 1.0f,
            KeyFrameIntervalKey, 15 * 60);
        Format mouseFormat = new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
            FrameRateKey, Rational.valueOf(frameRate));

        Dimension screenSize = videoConfig.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        try {
            return MonteRecorderBuilder
                .builder()
                .cfg(getGraphicConfig())
                .rectangle(captureSize)
                .fileFormat(fileFormat)
                .screenFormat(screenFormat)
                .folder(new File(videoConfig.getVideoFolder()))
                .mouseFormat(mouseFormat).build();
        } catch (IOException | AWTException e) {
            throw new RecorderException(e);

        }
    }
}
