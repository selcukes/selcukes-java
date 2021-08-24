/*
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
 */

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
    private Recorder recorder;
    private Boolean record;
    public GhostProxy(RegistrationRequest request, GridRegistry registry) {
        super(request, registry);
    }

    @Override
    public void beforeCommand(TestSession session, HttpServletRequest request,
                              HttpServletResponse response) {
        logger.info(() -> "Selenium Grid - Before Command");

    }


    @Override
    public void afterCommand(TestSession session, HttpServletRequest request,
                             HttpServletResponse response) {
        //ignore
    }

    @Override
    public void beforeSession(TestSession session) {
        logger.info(() -> "Selenium Grid - Before Session");
        record = (Boolean) session.getRequestedCapabilities().get("recordVideo");
        if (Boolean.TRUE.equals(record)) {
            recorder = RecorderFactory.getRecorder();
            recorder.start();
        }
    }

    @Override
    public void afterSession(TestSession session) {
        logger.info(() -> "Selenium Grid - After Session");
        if (Boolean.TRUE.equals(record))
            recorder.stopAndSave(session.getInternalKey());

    }

}
