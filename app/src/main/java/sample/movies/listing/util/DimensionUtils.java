package sample.movies.listing.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DimensionUtils {

  /**
   * Get the device screen width in pixels.
   */
  public static int getDeviceWidth(Activity activity) {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
    return displayMetrics.widthPixels;
  }
}