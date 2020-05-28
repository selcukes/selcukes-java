package io.github.selcukes.reports.video;

import lombok.Builder;

@Builder
public class FFmpegRecorderBuilder {
    String format;
    String display;
    int frameRate;
    String screenSize;
    String pixelFormat;
    String fileName;
}
