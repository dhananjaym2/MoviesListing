package sample.movies.listing.data;

import androidx.fragment.app.FragmentActivity;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.JSONException;
import sample.movies.listing.MoviesListingActivity;
import sample.movies.listing.errorHandling.AppErrorHandler;
import sample.movies.listing.log.AppLog;
import sample.movies.listing.util.FileUtils;

public class FetchInputDataRunnable implements Runnable {
  private String logTag = this.getClass().getSimpleName();
  private WeakReference<MoviesListingActivity> activityReference;

  public FetchInputDataRunnable(FragmentActivity fragmentActivity) {
    activityReference = new WeakReference<>((MoviesListingActivity) fragmentActivity);
  }

  @Override public void run() {
    String dataAsStringFromAssets = null;
    ArrayList<MovieItem> movieItemsList = new ArrayList<>();

    // read input data
    if (activityReference.get() != null) {
      try {
        dataAsStringFromAssets = FileUtils.readFileFromAssetsAsAString(activityReference.get(),
            "json/TestJSON.json", StandardCharsets.UTF_8);
      } catch (IOException e) {
        new AppErrorHandler(e);
      }
    } else {
      AppLog.error(logTag, "activityReference.get() is null, can't read data from assets");
    }

    // parse the data
    try {
      if (dataAsStringFromAssets != null) {
        JsonParser parser = new JsonParser();
        movieItemsList = parser.parseJsonFromString(dataAsStringFromAssets);
      } else {
        AppLog.error(logTag, "dataAsStringFromAssets is null");
      }
    } catch (JSONException e) {
      new AppErrorHandler(e);
    }

    // inform the UI with the new data.
    if (activityReference.get() != null) {
      activityReference.get().updateInputDataResult(movieItemsList);
    } else {
      AppLog.error(logTag, "activityReference.get() is null, can't notify caller");
    }
  }
}