package io.github.selcukes.reports.video;

import io.github.selcukes.core.config.Environment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@Builder
public class VideoConfig extends Environment {
    @Builder.Default
    String videoFolder="video";
    @Builder.Default
    boolean isVideoEnabled=false;
    @Builder.Default
    int frameRate=24;
    @Builder.Default
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();

}
