package io.github.selcukes.reports.notification.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@AllArgsConstructor
@Getter
@ToString
@Builder(builderClassName = "Builder")
class Field {
    private final String title;
    private final String value;
    @JsonProperty("short")
    private final Boolean shortValue;
}
