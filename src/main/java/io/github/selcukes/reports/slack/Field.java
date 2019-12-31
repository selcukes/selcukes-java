package io.github.selcukes.reports.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@AllArgsConstructor
@Getter
@ToString
@Builder(builderClassName = "Builder")
class Field {
    private String title;
    private String value;
    @JsonProperty("short")
    private Boolean shortValue;
}
