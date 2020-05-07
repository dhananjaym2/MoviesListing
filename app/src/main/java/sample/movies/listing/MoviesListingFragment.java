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
import java.util.ArrayList;
import sample.movies.listing.data.FetchInputDataRunnable;
import sample.movies.listing.data.MovieItem;
import sample.movies.listing.databinding.FragmentMoviesListingBinding;

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

  void updateInputDataResult(final ArrayList<MovieItem> movieItems) {
    requireActivity().runOnUiThread(new Runnable() {
      @Override public void run() {
        if (movieList != null) {
          movieList.addAll(movieItems);
        }
        if (moviesRecyclerAdapter != null) {
          moviesRecyclerAdapter.notifyDataSetChanged();
        }
      }
    });
  }
}