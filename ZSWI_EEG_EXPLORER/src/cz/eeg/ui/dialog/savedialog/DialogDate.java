package cz.eeg.ui.dialog.savedialog;

import static java.util.Calendar.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogDate {

	private static Calendar cal;
	private static Calendar now;

	static {
		cal = Calendar.getInstance();
		now = (Calendar) Calendar.getInstance().clone();
	}

	public static int getYear() {
		return cal.get(YEAR);
	}

	public static int getMonth() {
		return cal.get(MONTH) + 1;
	}

	public static int getDay() {
		return cal.get(DAY_OF_MONTH);
	}

	public static void setYear(int year) {
		cal.set(YEAR, year);
	}

	public static void setMonth(int month) {
		cal.set(MONTH, month - 1);
	}

	public static void setDay(int day) {
		cal.set(DAY_OF_MONTH, day);
	}

	public static int getMaxYear() {
		return  now.get(YEAR);
	}

	public static int getMaxMonth() {
		return  ( now.get(YEAR) == cal.get(YEAR)
				? now.get(MONTH) + 1
				: cal.getActualMaximum(MONTH) + 1);
	}

	public static int getMaxDay() {
		return  ( now.get(YEAR) == cal.get(YEAR) 
			   && now.get(MONTH) == cal.get(MONTH)
				? now.get(DAY_OF_MONTH)
				: cal.getActualMaximum(DAY_OF_MONTH));
	}
}
