package sample.movies.listing;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.movies.listing.data.MovieItem;
import sample.movies.listing.data.constants.DataConstants;
import sample.movies.listing.databinding.FragmentMoviesListingBinding;
import sample.movies.listing.errorHandling.AppErrorHandler;
import sample.movies.listing.log.AppLog;
import sample.movies.listing.util.DateTimeUtils;
import sample.movies.listing.util.FileUtils;

public class MoviesListingFragment extends Fragment {

  private ArrayList<MovieItem> movieList = new ArrayList<>();
  private final String logTag = this.getClass().getSimpleName();
  private FragmentMoviesListingBinding binding;
  private RecyclerView moviesRecyclerView;
  private MoviesRecyclerAdapter moviesRecyclerAdapter;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_listing, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initView(view);

    fetchData();
  }

  private void initView(@NonNull View view) {
    moviesRecyclerAdapter =
        new MoviesRecyclerAdapter(movieList, requireActivity());
    moviesRecyclerView = view.findViewById(R.id.moviesRecyclerView);
    moviesRecyclerView.setLayoutManager(
        new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
    binding.moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
  }

  private void fetchData() {
    Runnable fetchInputData = new FetchInputDataRunnable(requireActivity());
    Thread thread = new Thread(fetchInputData);
    thread.start();
  }

  private ArrayList<MovieItem> parseJsonFromString(String jsonDataAsString) throws JSONException {
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

  private class FetchInputDataRunnable implements Runnable {

    private WeakReference<MoviesListingActivity> activityReference;

    FetchInputDataRunnable(Activity activity) {
      activityReference = new WeakReference<>((MoviesListingActivity) activity);
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
          movieItemsList = parseJsonFromString(dataAsStringFromAssets);
        } else {
          AppLog.error(logTag, "dataAsStringFromAssets is null");
        }
      } catch (JSONException e) {
        new AppErrorHandler(e);
      }

      // update the UI with the new data.
      if (activityReference.get() != null) {
        final ArrayList<MovieItem> finalMovieItemsList = movieItemsList;
        activityReference.get().runOnUiThread(new Runnable() {
          @Override public void run() {
            movieList.addAll(finalMovieItemsList);
            moviesRecyclerAdapter.notifyDataSetChanged();
          }
        });
      } else {
        AppLog.error(logTag, "activityReference.get() is null, can't notify adapter¬");
      }
    }
  }
}