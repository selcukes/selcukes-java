package io.github.selcukes.core.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
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
}
