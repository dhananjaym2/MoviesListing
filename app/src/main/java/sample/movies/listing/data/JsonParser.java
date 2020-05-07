package sample.movies.listing.data;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.movies.listing.data.constants.DataConstants;
import sample.movies.listing.log.AppLog;
import sample.movies.listing.util.DateTimeUtils;

class JsonParser {

  private final String logTag = this.getClass().getSimpleName();

  ArrayList<MovieItem> parseJsonFromString(String jsonDataAsString) throws JSONException {
    AppLog.debug(logTag, "in parseJsonFromString():");
    JSONObject json = new JSONObject(jsonDataAsString);
    ArrayList<MovieItem> movieItemsArrayList = new ArrayList<>();

    if (json.has(DataConstants.testData)) {
      JSONArray jsonArray = json.optJSONArray(DataConstants.testData);

      if (jsonArray == null) {
        AppLog.warn(logTag, String.format("jsonArray is null from: %s", DataConstants.testData));
        return movieItemsArrayList;
      }

      for (int index = 0; index < jsonArray.length(); index++) {
        JSONObject movieJsonObject = jsonArray.optJSONObject(index);

        movieItemsArrayList.add(
            new MovieItem(
                movieJsonObject.optString(DataConstants.name),
                movieJsonObject.optString(DataConstants.payment_plan),
                movieJsonObject.optInt(DataConstants.release_year),
                movieJsonObject.optString(DataConstants.video_duration),
                movieJsonObject.optString(DataConstants.type),
                DateTimeUtils.getDateFromTimeString(
                    movieJsonObject.optString(DataConstants.created_on)),
                DateTimeUtils.getDateFromTimeString(
                    movieJsonObject.optString(DataConstants.updated_on)),
                movieJsonObject.optString(DataConstants.posterLink),
                movieJsonObject.optString(DataConstants.shortDescription),
                movieJsonObject.optString(DataConstants.description)
            )
        );
      }
    }
    AppLog.debug(logTag, "movieItemsArrayList.size():" + movieItemsArrayList.size());
    return movieItemsArrayList;
  }
}
