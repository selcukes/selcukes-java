package io.github.selcukes.core.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class DateHelper {
    private static final String DEFAULT_DATE_PATTERN = "ddMMMyyyy-hh-mm-ss";
    @Builder.Default
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    @Builder.Default
    private LocalDate localDate = LocalDate.now();
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();

    public String getDateTime() {
        return localDateTime.format(dtf);
    }

    public String getFutureDate(int noOfDays) {
        return localDate.plusDays(noOfDays).toString();
    }

    public String getPastDate(int noOfDays) {
        return localDate.minusDays(noOfDays).toString();
    }
    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
    }
}
