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

import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.RecorderException;
import io.github.selcukes.commons.exec.Shell;
import io.github.selcukes.commons.helper.DateHelper;
import io.github.selcukes.commons.helper.FileHelper;
import io.github.selcukes.commons.helper.Preconditions;
import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.commons.os.Platform;
import io.github.selcukes.video.config.DefaultVideoOptions;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

class FFmpegRecorder implements VideoRecorder {
    private static final Logger logger = LoggerFactory.getLogger(FFmpegRecorder.class);
    private static final String FFMPEG = "ffmpeg";
    private static final String EXTENSION = ".mp4";
    private final DefaultVideoOptions videoConfig;
    private final Shell shell;
    private File tempFile;
    private String ffmpegFolderPath = "";

    public FFmpegRecorder() {
        shell = new Shell();
        this.videoConfig = VideoRecorder.videoConfig();
        if (ConfigFactory.getConfig().getVideo().getFfmpegPath() != null) {
            ffmpegFolderPath = ConfigFactory.getConfig().getVideo().getFfmpegPath();
        }
    }

    public void start() {

        tempFile = getFile("Video");
        String screenSize = getScreenSize();

        String command = ffmpegFolderPath + FFMPEG + " -y " +
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
    public File stopAndSave(final String filename) {
        stop();
        Preconditions.checkArgument(tempFile.exists(), "Video recording wasn't started");
        File videoFile = getFile(filename);
        if (ConfigFactory.getConfig().getVideo().isWatermark()) {
            File waterMark = FileHelper.getWaterMarkFile();
            String command = ffmpegFolderPath + FFMPEG + " -i " + tempFile.getAbsolutePath() +
                    " -i " + waterMark.getAbsolutePath() +
                    " -filter_complex \"overlay=x=(main_w-200):y=5\" " + videoFile.getAbsolutePath();
            shell.runCommand(command);
            tempFile.deleteOnExit();
            Await.until(1);
        } else {
            try {
                FileHelper.renameFile(tempFile, videoFile);
            } catch (RecorderException ex) {
                Await.until(2);
                FileHelper.renameFile(tempFile, videoFile);
            }
        }
        logger.info(() -> "Recording finished to " + videoFile.getAbsolutePath());
        return videoFile;
    }

    private void stop() {
        logger.info(() -> "Killing ffmpeg...");
        String killFFmpeg = "pkill -INT ffmpeg";
        if (Platform.isWindows()) {
            shell.sendCtrlC(ffmpegFolderPath);
        } else {
            shell.runCommand(killFFmpeg);
        }
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

