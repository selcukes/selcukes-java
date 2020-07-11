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


import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.exec.Shell;
import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.reports.config.VideoConfig;

import java.awt.*;
import java.io.File;


class FFmpegRecorder extends VideoRecorder {
    private static final Logger logger = LoggerFactory.getLogger(FFmpegRecorder.class);
    private static final String FFMPEG = "ffmpeg";
    private static final String EXTENSION = ".mp4";
    private final VideoConfig videoConfig;
    private final Shell shell;
    private File tempFile;

    public FFmpegRecorder() {
        shell = new Shell();
        this.videoConfig = conf();
    }

    public void start() {

        tempFile = getFile("Video");
        String screenSize = getScreenSize();

        String command = FFMPEG + " -y " +
            "-video_size " + screenSize +
            " -f " + videoConfig.getFfmpegFormat() +
            " -i " + videoConfig.getFfmpegDisplay() +
            " -an " +
            " -framerate " + videoConfig.getFrameRate() +
            " -pix_fmt " + videoConfig.getPixelFormat() + " " +
            tempFile.getAbsolutePath();
        shell.runCommandAsync(command);
        logger.info(() -> "Recording started to " + tempFile.getAbsolutePath());
    }

    @Override
    public File stopAndSave(String filename) {
        stop();
        Preconditions.checkArgument(tempFile.exists(), "Video recording wasn't started");
        File videoFile = getFile(filename);
        File waterMark = FileHelper.loadResource("selcukes-watermark.png");

        String command = FFMPEG + " -i " + tempFile.getAbsolutePath() +
            " -i " + waterMark.getAbsolutePath() +
            " -filter_complex \"overlay=x=(main_w-200):y=5\" " + videoFile.getAbsolutePath();
        shell.runCommand(command);
        tempFile.deleteOnExit();
        Await.until(1);
        logger.info(() -> "Recording finished to " + videoFile.getAbsolutePath());
        return videoFile;
    }

    private void stop() {
        logger.info(() -> "Killing ffmpeg...");
        String killFFmpeg = "pkill -INT ffmpeg";
        if (Platform.isWindows()) shell.sendCtrlC();
        else shell.runCommand(killFFmpeg);
        Await.until(2);
    }

    @Override
    public void stopAndDelete() {
        stop();
        logger.info(() -> "Deleting recorded video file...");
        tempFile.deleteOnExit();
    }

    private String getScreenSize() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return dimension.width + "x" + dimension.height;
    }

    private File getFile(String fileName) {
        File movieFolder = createMovieFolder();
        String name = fileName + "_recording_" + DateHelper.get().dateTime();
        return new File(movieFolder + File.separator + name + EXTENSION);
    }

    private File createMovieFolder() {
        File movieFolder = new File(videoConfig.getVideoFolder());
        FileHelper.createDirectory(movieFolder);
        return movieFolder;
    }

}

