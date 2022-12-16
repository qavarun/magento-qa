package utils;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DateTimeMethods {

	public String getCurrentDate() {
		DateFormat dateformat = new SimpleDateFormat("yyyy-MMM-d");
		Date date = new Date();
		return dateformat.format(date).toUpperCase();
	}

	public String getCurrentDateWithRequiredFormat(String format) {
		DateFormat dateformat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateformat.format(date).toUpperCase();
	}

	public String getCurrentDateWithFormat() {
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateformat.format(date);
	}

	// First parameter for month, Second parameter for date, Third parameter for minute
	public String getCurrentDateWithDashFormat(int... monthsdays) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if (monthsdays.length == 3) {
			c.add(Calendar.MONTH, monthsdays[0]);
			c.add(Calendar.DATE, monthsdays[1]);
			c.add(Calendar.MINUTE, monthsdays[2]);
		}
		if (monthsdays.length == 2) {
			c.add(Calendar.MONTH, monthsdays[0]);
			c.add(Calendar.DATE, monthsdays[1]);
		}
		if (monthsdays.length == 1) {
			c.add(Calendar.MONTH, monthsdays[0]);
			c.add(Calendar.DATE, 0);
		}
		if (monthsdays.length == 0) {
			c.add(Calendar.MONTH, 0);
			c.add(Calendar.DATE, 0);
		}
		return sdf.format(c.getTime());
	}

	public String getEndDate(int difference) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-d");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, difference); // Adding 3 Days
		String output = sdf.format(c.getTime());
		return output.toUpperCase();
	}

	public String getEndDateWithFormat() {
		DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 3); // Adding 3 Days
		String output = dateformat.format(c.getTime());
		return output.toUpperCase();
	}

	public String getCurrentTime() {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		return timeFormat.format(date);
	}

	public String getCurrentDateWithTime() {
		SimpleDateFormat date = new SimpleDateFormat("ddMMyyyyHHmmss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return date.format(timestamp);
	}

	public String getDateWithMonthName(String str) throws ParseException {
		String s = str.substring(0, str.indexOf(" "));
		Date date = new SimpleDateFormat("yyyy-MM-d").parse(s);
		DateFormat df = new SimpleDateFormat("MMMM d yyyy");
		Date convertDate = date;
		return df.format(convertDate);
	}

	public String getDateWithRequiredMonthAndDate(int month, int date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-d");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, month);
		c.set(Calendar.DATE, date);
		String output = sdf.format(c.getTime());
		return output.toUpperCase();
	}

	public int getWeekNumber(String input, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = df.parse(input);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public String getRequiredDateAndTime(int... minutes) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if (minutes.length == 0) {
			c.add(Calendar.MINUTE, 0);
		} else {
			c.add(Calendar.MINUTE, minutes[0]);
		}
		return timeFormat.format(c.getTime());
	}

	public String getMonthNameFromDate(String str) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-d");
		Date date = df.parse(str);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		return monthNames[cal.get(Calendar.MONTH)];
	}

	public String getCurrentDateWithOwnFormat(String format) {
		DateFormat dateformat = new SimpleDateFormat(format);
		Date date = new Date();
		String currentDate = dateformat.format(date).toUpperCase();
		return currentDate;
	}

	public String getEndDate(int difference, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, difference); // Adding 3 days
		String output = sdf.format(c.getTime());
		return output.toUpperCase();
	}

	public String getCurrentDateWithUserFormate(String format) {
		DateFormat dateformat = new SimpleDateFormat(format);
		Date date = new Date();
		String currentDate = dateformat.format(date);
		return currentDate;
	}

	public String getETDateTimeWithDayName() {
		DateTimeFormatter etFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
		ZoneId etZoneId = ZoneId.of("US/Eastern");
		LocalDateTime currentDateTime = LocalDateTime.now();
		ZonedDateTime currentISTime = currentDateTime.atZone(istZoneId);
		ZonedDateTime currentETime = currentISTime.withZoneSameInstant(etZoneId);
		String str = currentETime.getDayOfWeek().toString().toLowerCase();
		StringBuffer dayName = new StringBuffer(str);
		dayName.setCharAt(0, Character.toUpperCase(str.charAt(0)));
		return (etFormat.format(currentETime) + " " + dayName);
	}

	public String getAddedHourTimeAsPerETTime(int nexthour, int nextDay) {
		DateTimeFormatter etFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		ZoneId etZoneId = ZoneId.of("US/Eastern");
		ZonedDateTime now = ZonedDateTime.now(etZoneId);
		ZonedDateTime hourLater = now.plusHours(nexthour).plusDays(nextDay);
		String str = hourLater.getDayOfWeek().toString().toLowerCase();
		StringBuffer dayName = new StringBuffer(str);
		dayName.setCharAt(0, Character.toUpperCase(str.charAt(0)));
		return (etFormat.format(hourLater) + " " + dayName);
	}

	public String getTimeWithTimeZone(String timeZone, String format) {  // modified by sel+8
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		String utcTime = sdf.format(new Date());
		return utcTime;
	}
}