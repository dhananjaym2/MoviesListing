package sample.movies.listing;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MoviesListingActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.hostFrameLayout, new MoviesListingFragment(),
            MoviesListingFragment.class.getSimpleName());
  }
}