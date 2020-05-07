package sample.movies.listing.log;

import android.util.Log;
import sample.movies.listing.BuildConfig;

public class AppLog {

  private static void log(int priority, String logTag, String message) {
    if (!BuildConfig.DEBUG) {
      return;
    }
    switch (priority) {
      case Log.VERBOSE:
        Log.v(logTag, message);
        break;

      case Log.DEBUG:
        Log.d(logTag, message);
        break;

      case Log.WARN:
        Log.w(logTag, message);
        break;

      case Log.ERROR:

      default:
        Log.e(logTag, message);
        break;
    }
  }

  public static void warn(String logTag, String message) {
    log(Log.WARN, logTag, message);
  }

  public static void error(String logTag, String message) {
    log(Log.ERROR, logTag, message);
  }

  public static void debug(String logTag, String message) {
    log(Log.DEBUG, logTag, message);
  }

  public static void verbose(String logTag, String message) {
    log(Log.VERBOSE, logTag, message);
  }
}
