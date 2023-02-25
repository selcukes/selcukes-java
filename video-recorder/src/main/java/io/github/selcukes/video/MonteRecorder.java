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

import io.github.selcukes.commons.exception.RecorderException;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.video.config.DefaultVideoOptions;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.VideoFormatKeys;
import org.monte.media.math.Rational;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import static org.monte.media.VideoFormatKeys.MediaType;

class MonteRecorder implements VideoRecorder {
    private final Logger logger = LoggerFactory.getLogger(MonteRecorder.class);
    private final MonteRecorderBuilder recorderBuilder;
    private final DefaultVideoOptions videoConfig;

    public MonteRecorder() {
        this.videoConfig = VideoRecorder.videoConfig();
        this.recorderBuilder = getScreenRecorder();
    }

    private static File encodeRecording(String sourcePath) {
        File source = new File(sourcePath);
        File target = new File(sourcePath.replace("avi", "mp4"));
        try {

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libvorbis");
            VideoAttributes video = new VideoAttributes();
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp4");
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            throw new RecorderException("Failed converting Video to mp4 format.");
        }
        return target;
    }

    @Override
    public void start() {
        recorderBuilder.start();
        logger.info(() -> "Recording started");
    }

    @Override
    public File stopAndSave(String filename) {
        File video = writeVideo(filename);
        File mp4Video = encodeRecording(video.getAbsolutePath());
        video.deleteOnExit();
        logger.info(() -> "Recording finished to " + mp4Video.getAbsolutePath());
        return mp4Video;
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

        var fileFormat = new Format(
            FormatKeys.MediaTypeKey, MediaType.VIDEO,
            FormatKeys.MimeTypeKey, FormatKeys.MIME_AVI);
        var screenFormat = new Format(
            FormatKeys.MediaTypeKey, MediaType.VIDEO,
            FormatKeys.EncodingKey, VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
            VideoFormatKeys.CompressorNameKey, VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
            VideoFormatKeys.DepthKey, 24,
            FormatKeys.FrameRateKey, Rational.valueOf(frameRate),
            VideoFormatKeys.QualityKey, 1.0f,
            FormatKeys.KeyFrameIntervalKey, 15 * 60);
        var mouseFormat = new Format(
            FormatKeys.MediaTypeKey, MediaType.VIDEO,
            FormatKeys.EncodingKey, "black",
            FormatKeys.FrameRateKey, Rational.valueOf(frameRate));

        var screenSize = videoConfig.getScreenSize();
        var captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);

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
