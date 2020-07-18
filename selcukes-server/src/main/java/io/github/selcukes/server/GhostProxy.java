package io.github.selcukes.server;

import io.github.selcukes.commons.logging.Logger;
import io.github.selcukes.commons.logging.LoggerFactory;
import io.github.selcukes.reports.video.Recorder;
import io.github.selcukes.reports.video.RecorderFactory;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.listeners.TestSessionListener;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GhostProxy extends DefaultRemoteProxy implements TestSessionListener {
    private final Logger logger = LoggerFactory.getLogger(GhostProxy.class);

    public GhostProxy(RegistrationRequest request, GridRegistry registry) {
        super(request, registry);
    }

    private static Recorder recorder;
    private static Boolean record;

    @Override
    public void beforeCommand(TestSession session, HttpServletRequest request,
                              HttpServletResponse response) {
        logger.info(() -> "Selenium Grid - Before Command");

    }


    @Override
    public void afterCommand(TestSession session, HttpServletRequest request,
                             HttpServletResponse response) {
    }

    @Override
    public void beforeSession(TestSession session) {
        logger.info(() -> "Selenium Grid - Before Session");
        record = (Boolean) session.getRequestedCapabilities().get("recordVideo");
        if (record) {
            recorder = RecorderFactory.getRecorder();
            recorder.start();
        }
    }

    @Override
    public void afterSession(TestSession session) {
        logger.info(() -> "Selenium Grid - After Session");
        if (record)
            recorder.stopAndSave(session.getInternalKey());

    }

}
