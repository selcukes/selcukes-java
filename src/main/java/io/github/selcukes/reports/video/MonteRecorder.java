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
import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

class MonteRecorder extends ScreenRecorder {

    private String currentTempExtension;

    MonteRecorder(GraphicsConfiguration cfg,
                  Format fileFormat,
                  Format screenFormat,
                  Format mouseFormat,
                  Format audioFormat,
                  File folder) throws IOException, AWTException {
        super(cfg, fileFormat, screenFormat, mouseFormat, audioFormat);
        super.movieFolder = folder;
    }

    MonteRecorder(GraphicsConfiguration cfg,
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
        DateHelper dateHelper = DateHelper.builder().build();
        String fileName = filename + "_recording_" + dateHelper.getDateTime();
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
