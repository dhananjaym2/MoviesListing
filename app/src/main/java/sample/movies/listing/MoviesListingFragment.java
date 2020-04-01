package sample.movies.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesListingFragment extends Fragment {

  private RecyclerView moviesRecyclerView;
  //private MoviesRecyclerAdapter moviesRecyclerAdapter;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState
  ) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_movies_listing, container, false);
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    moviesRecyclerView = view.findViewById(R.id.moviesRecyclerView);
    //moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
  }
}