package sample.movies.listing.util;

import android.text.TextUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import sample.movies.listing.errorHandling.AppErrorHandler;
import sample.movies.listing.log.AppLog;

public class DateTimeUtils {
  private static final String logTag = DateTimeUtils.class.getSimpleName();

  public static String getDateFromTimeString(String inputDate) {
    if (!TextUtils.isEmpty(inputDate)) {
      final String inputDateTimeFormat = "yyyy-MM-dd";
      DateFormat originalFormat = new SimpleDateFormat(inputDateTimeFormat, Locale.getDefault());
      //originalFormat.setTimeZone(timeZone);
      try {
        Date date = originalFormat.parse(inputDate);
        final String outputFormat = "yy-MM-dd";
        String formattedDate = android.text.format.DateFormat.format(outputFormat, date).toString();
        AppLog.debug(logTag, "inputDate: " + inputDate + " formattedDate: " + formattedDate);
        return formattedDate;
      } catch (ParseException | NullPointerException e) {
        new AppErrorHandler(e);
      }
    }
    return inputDate;
  }
}