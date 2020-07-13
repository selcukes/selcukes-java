package io.github.selcukes.reports.screenshot;

import lombok.Builder;

@Builder
public class ScreenGrabber {
    String text;
    String fileName;
    boolean addressBar;
    boolean fullPage;
}
