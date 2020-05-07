package sample.movies.listing;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import sample.movies.listing.data.MovieItem;
import sample.movies.listing.log.AppLog;

public class MoviesListingActivity extends AppCompatActivity {

  private final String moviesListFragmentTag = MoviesListingFragment.class.getSimpleName();
  private final String logTag = this.getClass().getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.hostFrameLayout, new MoviesListingFragment(), moviesListFragmentTag)
        .commit();
  }

  public void updateInputDataResult(ArrayList<MovieItem> movieItems) {
    Fragment fragment = getSupportFragmentManager().findFragmentByTag(moviesListFragmentTag);
    if (fragment instanceof MoviesListingFragment) {
      ((MoviesListingFragment) fragment).updateInputDataResult(movieItems);
    } else {
      AppLog.error(logTag,
          "fragment is not instanceof MoviesListingFragment in updateInputDataResult()");
    }
  }
}