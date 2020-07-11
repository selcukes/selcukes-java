package io.github.selcukes.grid;

import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.VideoRecorder;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.listeners.TestSessionListener;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GhostProxy extends DefaultRemoteProxy implements TestSessionListener {

    public GhostProxy(RegistrationRequest request, GridRegistry registry) {
        super(request, registry);
    }

    private static Recorder recorder;
    private static String RECORD_VIDEO = "recordVideo";
    private static Boolean record;
    private static String folderNamebySession;

    @Override
    public void beforeCommand(TestSession session, HttpServletRequest request,
                              HttpServletResponse response) {
        System.out.println("Selenium Grid - Before Command");

    }


    @Override
    public void afterCommand(TestSession session, HttpServletRequest request,
                             HttpServletResponse response) {
    }

    @Override
    public void beforeSession(TestSession session) {
        record = (Boolean) session.getRequestedCapabilities().get(RECORD_VIDEO);
        if (record) {
            recorder = VideoRecorder.monteRecorder();
            recorder.start();
            System.out.println("Video Recording value is.." + record);
        }
    }

    @Override
    public void afterSession(TestSession session) {
        System.out.println("Selenium Grid - After Session");
        if (record)
            recorder.stopAndSave(session.getInternalKey());

    }

}
