package io.github.selcukes.reports.video;

import io.github.selcukes.core.exception.RecorderException;
import io.github.selcukes.core.helper.DateHelper;
import io.github.selcukes.core.logging.Logger;
import io.github.selcukes.core.logging.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class FFmpegRecorder {
    final Logger logger = LoggerFactory.getLogger(FFmpegRecorder.class);
    private Process process;


    public String getFilePath() {
        DateHelper dateHelper = DateHelper.builder().build();
        return System.getProperty("user.dir") + File.separator + "ffmpeg" + "_recording_" + dateHelper.getDateTime() + ".mp4";
    }

    public void start() {

        String fileName = getFilePath();
        String pixelFormat = "yuv420p";
        int frameRate = 24;
        String ffmpegFormat = "gdigrab";
        String ffmpegDisplay = "desktop";
        String screenSize = getScreenSize();

        String cmdline = "ffmpeg " + "-y " +
            "-video_size " + screenSize +
            " -f " + ffmpegFormat +
            " -i " + ffmpegDisplay +
            " -an " +
            " -framerate " + frameRate +
            " -pix_fmt " + pixelFormat + " " +
            fileName;

        logger.info(() -> "Recording video to " + fileName);
        process = runCommand(cmdline);
        logger.info(() -> "Started ffmpeg");
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

    public String getPid(Process p) {
        String pid;
        String first =  process.toString().split(",")[0];
        pid = first.split("=")[1];
        logger.info(() -> pid);
        return pid;
    }

    public void stop() {
        String kill = "SendSignalCtrlC.exe " + getPid(process);
        runCommand(kill);
    }


    private String getScreenSize() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        return dimension.width + "x" + dimension.height;
    }

}

