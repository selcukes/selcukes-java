package io.github.selcukes.reports.video;

import java.io.File;

public interface IRecorder {
    void start();
    File stopAndSave(String filename);
    void stopAndDelete(String filename);
}
