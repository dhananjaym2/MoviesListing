package sample.movies.listing;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.movies.listing.data.MovieItem;
import sample.movies.listing.data.constants.DataConstants;
import sample.movies.listing.databinding.FragmentMoviesListingBinding;
import sample.movies.listing.errorHandling.AppErrorHandler;
import sample.movies.listing.log.AppLog;
import sample.movies.listing.util.DateTimeUtils;
import sample.movies.listing.util.DimensionUtils;
import sample.movies.listing.util.FileUtils;

public class MoviesListingFragment extends Fragment {

  private RecyclerView moviesRecyclerView;
  //private MoviesRecyclerAdapter moviesRecyclerAdapter;
  private List<MovieItem> movieList;
  private final String logTag = MoviesListingFragment.class.getSimpleName();
  private FragmentMoviesListingBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    AppLog.debug(logTag, "in onCreateView()");
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies_listing, container, false);
    View view = binding.getRoot();
    return view;
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    AppLog.debug(logTag, "in onViewCreated()");
    super.onViewCreated(view, savedInstanceState);

    moviesRecyclerView = view.findViewById(R.id.moviesRecyclerView);

    String dataAsStringFromAssets = null;
    try {
      if (getActivity() != null) {
        dataAsStringFromAssets = FileUtils.readFileFromAssetsAsAString(getActivity(),
            "json/TestJSON.json", StandardCharsets.UTF_8);
      } else {
        AppLog.error(logTag, "getActivity() returns null");
      }
    } catch (IOException e) {
      new AppErrorHandler(e);
      //return;
    }
    try {
      if (dataAsStringFromAssets != null) {
        movieList = parseJsonFromString(dataAsStringFromAssets);
      }
    } catch (JSONException e) {
      new AppErrorHandler(e);
    }
    MoviesRecyclerAdapter moviesRecyclerAdapter =
        new MoviesRecyclerAdapter(movieList, getActivity(), getImageViewWidth());
    RecyclerView.LayoutManager layoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    moviesRecyclerView.setLayoutManager(layoutManager);
    binding.moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
  }

  private int getImageViewWidth() {
    return DimensionUtils.getDeviceWidth(requireActivity()) / 2;
  }

  private ArrayList<MovieItem> parseJsonFromString(String jsonDataAsString) throws JSONException {
    AppLog.debug(logTag, "in parseJsonFromString():");
    JSONObject json = new JSONObject(jsonDataAsString);
    ArrayList<MovieItem> movieList = new ArrayList<>();

    if (json.has(DataConstants.testData)) {
      JSONArray jsonArray = json.optJSONArray(DataConstants.testData);

      if (jsonArray == null) {
        AppLog.warn(logTag, String.format("jsonArray is null from: %s", DataConstants.testData));
        return movieList;
      }

      for (int index = 0; index < jsonArray.length(); index++) {
        JSONObject movieJsonObject = jsonArray.optJSONObject(index);

        movieList.add(
            new MovieItem(
                movieJsonObject.optInt(DataConstants.id),
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
    AppLog.debug(logTag, "movieList.size():" + movieList.size());
    return movieList;
  }
}