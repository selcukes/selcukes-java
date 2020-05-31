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
import io.github.selcukes.core.helper.DateHelper;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class FFmpegRecorder extends Recorder {
    final Logger logger = LoggerFactory.getLogger(FFmpegRecorder.class);
    private Process process;
    private final String FFMPEG = "ffmpeg";
    private final VideoConfig videoConfig;
    private File videoFile;

    public FFmpegRecorder() {
        this.videoConfig = conf();
    }


    /**
     * This method will start the recording of the execution.
     */
    public void start() {

        String fileName = getFilePath();
        String screenSize = getScreenSize();

        String cmdline = FFMPEG + " -y " +
            "-video_size " + screenSize +
            " -f " + videoConfig.getFfmpegFormat() +
            " -i " + videoConfig.getFfmpegDisplay() +
            " -an " +
            " -framerate " + videoConfig.getFrameRate() +
            " -pix_fmt " + videoConfig.getPixelFormat() + " " +
            fileName;

        logger.info(() -> "Recording video started to " + fileName);
        process = runCommand(cmdline);
        logger.info(() -> "Started ffmpeg...");
    }

    /**
     * This method will stop and save's the recording.
     */
    @Override
    public File stopAndSave(String filename) {
        String kill = "SendSignalCtrlC.exe " + getPid(process);
        runCommand(kill);
        logger.info(() -> "Killing ffmpeg...");
        videoFile = new File(getFilePath());
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


    public Process runCommand(String command) {
        logger.info(() -> "Trying to execute command : " + command);
        Process process;
        ProcessBuilder pb = new ProcessBuilder().inheritIO().command(command.split("\\s"));
        pb.redirectOutput(Redirect.PIPE);
        pb.redirectError(Redirect.PIPE);
        try {
            synchronized (this) {
                process = pb.start();
            }

        } catch (IOException ex) {
            logger.error(() -> "Unable to execute command: " + command);
            throw new RecorderException(ex.getMessage());
        }
        return process;
    }

    private String getPid(Process p) {
        String pid;
        String first = process.toString().split(",")[0];
        pid = first.split("=")[1];
        logger.info(() -> pid);
        return pid;
    }

    private String getScreenSize() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return dimension.width + "x" + dimension.height;
    }

    public String getFilePath() {
        DateHelper dateHelper = DateHelper.builder().build();
        String fileName = FFMPEG + "_recording_" + dateHelper.getDateTime();
        return System.getProperty("user.dir") + File.separator + videoConfig.getVideoFolder() + fileName + ".mp4";
    }

}

