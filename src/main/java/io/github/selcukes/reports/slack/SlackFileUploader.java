package io.github.selcukes.reports.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackFileUploader {
    private String channel;
    private String token;
    private String filePath;
    private String fileName;
}
