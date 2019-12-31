package io.github.selcukes.reports.video;

import io.github.selcukes.core.exception.RecorderException;
import io.github.selcukes.core.helper.DateHelper;
import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

class MonteScreenRecorder extends ScreenRecorder {

    private String currentTempExtension;

    MonteScreenRecorder(GraphicsConfiguration cfg,
                        Format fileFormat,
                        Format screenFormat,
                        Format mouseFormat,
                        Format audioFormat,
                        File folder) throws IOException, AWTException {
        super(cfg, fileFormat, screenFormat, mouseFormat, audioFormat);
        super.movieFolder = folder;
    }

    MonteScreenRecorder(GraphicsConfiguration cfg,
                        Rectangle rectangle,
                        Format fileFormat,
                        Format screenFormat,
                        Format mouseFormat,
                        Format audioFormat,
                        File folder) throws IOException, AWTException {
        super(cfg, rectangle, fileFormat, screenFormat, mouseFormat, audioFormat);
        super.movieFolder = folder;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        this.currentTempExtension = Registry.getInstance().getExtension(fileFormat);
        return super.createMovieFile(fileFormat);
    }

    public File saveAs(String filename) {
        this.stop();

        File tempFile = this.getCreatedMovieFiles().get(0);

        File destFile = getDestinationFile(filename);
        tempFile.renameTo(destFile);
        return destFile;
    }

    private File getDestinationFile(String filename) {
        String fileName = filename + "_recording_" + DateHelper.formatDate(new Date(), "yyyy_dd_MM_HH_mm_ss");
        return new File(this.movieFolder + File.separator + fileName + "." + this.currentTempExtension);

    }

    @Override
    public void start() {
        try {
            super.start();
        } catch (IOException e) {
            throw new RecorderException(e);
        }
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } catch (IOException e) {
            throw new RecorderException(e);
        }
    }
}
