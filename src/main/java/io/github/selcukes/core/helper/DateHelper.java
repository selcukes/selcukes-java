package io.github.selcukes.core.helper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Logger;

public class DateHelper {
	Logger logger = Logger.getLogger(DateHelper.class.getName());
	private DateTimeFormatter dtf;
	private LocalDate today;
	private LocalDate date;
	LocalDateTime dateTime;

	public DateHelper(String date) {
		dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		today = LocalDate.parse(date, dtf);
	}

	public DateHelper() {
		dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		today = LocalDate.now();
	}

    public static String formatDate(Date date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				format);
		return dateFormat.format(date);
	}
    private String getDate() {
		return dtf.format(date);
	}

	private void setDate(LocalDate date) {
		this.date = date;
	}

	public String getCurrentDate() {
		this.setDate(today);
		return getDate();
	}

	public String getFutureDate(int noOfDays) {
		this.setDate(today.plus(noOfDays, ChronoUnit.DAYS));
		return getDate();
	}

	public String getPastDate(int noOfDays) {
		this.setDate(today.minusDays(noOfDays));
		return getDate();
	}

	// Method checks if this date is after the specified date.
	public boolean isAfter(String date) {
		LocalDate date1 = today;
		LocalDate date2 = new DateHelper(date).today;
		logger.info(dtf.format(date1) + " is after " + dtf.format(date2) + " : " + date1.isAfter(date2));
		return date1.isAfter(date2);

	}

	public static String convertDateFormat(String armisticeDateTime) {
		LocalDate localDate = LocalDate.parse(armisticeDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		return localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}

	public static String getCurrentDateTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		return currentTime.format(DateTimeFormatter.ofPattern("ddMMMyyyy-hh-mm-ss"));

	}

	public static String getDateTime(boolean isPast) {

		LocalDateTime currentTime = (isPast) ? LocalDateTime.now().minusDays(5)
				: LocalDateTime.now().plus(5, ChronoUnit.DAYS);
		return currentTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));

	}

}
