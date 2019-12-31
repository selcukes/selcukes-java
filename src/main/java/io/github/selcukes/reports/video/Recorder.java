package io.github.selcukes.reports.video;

import java.io.File;

public abstract class Recorder implements IRecorder {
    public static VideoConfig conf() {

        VideoConfig vc= VideoConfig.builder()
                .build();
        return vc;
    }

    private static File lastVideo;

    protected void setLastVideo(File video) {
        lastVideo = video;
    }

    public static File getLastRecording() {
        return lastVideo;
    }

}
