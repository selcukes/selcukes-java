package io.github.selcukes.core.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Environment {
    private String projectName;
    private String env;
    private String browserName;
    private String windowId;
    private Boolean headLess;
    private Boolean remote;
    private String isProxy;
    private String baseUrl;
    private String remoteGridUrl;
    private Boolean videoRecording;
    private Map<String, String> slack;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getWindowId() {
        return windowId;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    public Boolean getHeadLess() {
        return headLess;
    }

    public void setHeadLess(Boolean headLess) {
        this.headLess = headLess;
    }

    public Boolean getRemote() {
        return remote;
    }

    public void setRemote(Boolean remote) {
        this.remote = remote;
    }

    public String getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(String isProxy) {
        this.isProxy = isProxy;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRemoteGridUrl() {
        return remoteGridUrl;
    }

    public void setRemoteGridUrl(String remoteGridUrl) {
        this.remoteGridUrl = remoteGridUrl;
    }

    public Boolean getVideoRecording() {
        return videoRecording;
    }

    public void setVideoRecording(Boolean videoRecording) {
        this.videoRecording = videoRecording;
    }

    public Map<String, String> getSlack() {
        return slack;
    }

    public void setSlack(Map<String, String> slack) {
        this.slack = slack;
    }

}
