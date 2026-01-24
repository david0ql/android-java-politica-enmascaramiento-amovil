package co.com.amovil.masking.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
  private static final String DATE_PATTERN = "yyyy-MM-dd";

  private DateUtils() {
  }

  public static String shiftDate(String originalDate, int offsetDays) {
    if (originalDate == null) {
      return null;
    }
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.US);
    formatter.setLenient(false);
    try {
      Date parsed = formatter.parse(originalDate);
      if (parsed == null) {
        return originalDate;
      }
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(parsed);
      calendar.add(Calendar.DATE, offsetDays);
      return formatter.format(calendar.getTime());
    } catch (ParseException ex) {
      return originalDate;
    }
  }
}
