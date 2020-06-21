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


import io.github.selcukes.core.commons.exec.Shell;
import io.github.selcukes.core.commons.os.Platform;
import io.github.selcukes.core.helper.DateHelper;
import io.github.selcukes.core.helper.FileHelper;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;

import java.awt.*;
import java.io.File;

class FFmpegRecorderImpl extends AbstractRecorder {
    private static final Logger logger = LoggerFactory.getLogger(FFmpegRecorderImpl.class);
    private static final String FFMPEG = "ffmpeg";
    private static final String EXTENSION = ".mp4";
    private final VideoConfig videoConfig;
    private File videoFile;
    private final Shell shell;
    private File tempFile;

    public FFmpegRecorderImpl() {
        shell = new Shell();
        this.videoConfig = conf();
    }


    /**
     * This method will start the recording of the execution.
     */
    public void start() {

        tempFile = getFile("Video");
        String screenSize = getScreenSize();

        String cmdline = FFMPEG + " -y " +
            "-video_size " + screenSize +
            " -f " + videoConfig.getFfmpegFormat() +
            " -i " + videoConfig.getFfmpegDisplay() +
            " -an " +
            " -framerate " + videoConfig.getFrameRate() +
            " -pix_fmt " + videoConfig.getPixelFormat() + " " +
            tempFile.getAbsolutePath();

        logger.info(() -> "Recording video started to " + tempFile.getAbsolutePath());
        shell.runCommandAsync(cmdline);
        logger.info(() -> "Started ffmpeg...");
    }

    /**
     * This method will stop and save's the recording.
     */
    @Override
    public File stopAndSave(String filename) {

        String killFFmpeg = "pkill -INT ffmpeg";
        if (Platform.isWindows()) shell.sendCtrlC();
        else shell.runCommand(killFFmpeg);
        logger.info(() -> "Killing ffmpeg...");
        videoFile = getFile(filename);
        FileHelper.renameFile(tempFile, videoFile);
        logger.info(() -> "Recording finished to " + videoFile.getAbsolutePath());
        return videoFile;
    }

    /**
     * This method will delete the recorded file,if the test is pass.
     */
    @Override
    public void stopAndDelete(String filename) {
        stopAndSave(filename);
        logger.info(() -> "Trying to delete recorded video files...");
        videoFile.deleteOnExit();
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

