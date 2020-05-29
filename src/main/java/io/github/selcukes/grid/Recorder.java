package io.github.selcukes.grid;

import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;


public class Recorder {
    ScreenRecorder screenRecorder = null;
    public Recorder(){
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        try {
            screenRecorder = new ScreenRecorder(gc,
                    new Format(MediaTypeKey, FormatKeys.MediaType.FILE,
                            MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, FormatKeys.MediaType.VIDEO,
                            EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, COMPRESSOR_NAME_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24,
                            FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, (15 * 60)),
                    new Format(MediaTypeKey, FormatKeys.MediaType.VIDEO,
                            EncodingKey, "black",
                            FrameRateKey, Rational.valueOf(30)),
                    null);
        } catch (IOException e) {
            System.out.println("Recorder Object cannot be intialized");
        } catch (AWTException e) {
            System.out.println("Recorder Object cannot be intialized");
        }
    }

    protected void startScreenRecorder() {
        if (screenRecorder != null) {
            try {
                screenRecorder.start();
                System.out.println("Test Recording Started");
            } catch (IOException e) {
                System.out.println("Test Recording cannot be started");
            }
        }
    }

    protected void stopScreenRecorder(String recordingName) {
        if (screenRecorder != null) {
            try {
                screenRecorder.stop();
            } catch (IOException e) {
            }

            List<File> createdMovieFiles = screenRecorder.getCreatedMovieFiles();

            for (File movie : createdMovieFiles) {

                File dir = new File(System.getProperty("user.dir"));
                Path path;
                try {
                    path = Files.move(movie.toPath(), dir.toPath().resolve(recordingName + ".avi"), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Test Recording is saved in "+path);
                } catch (IOException e) {

                }
            }
        }
    }
}
