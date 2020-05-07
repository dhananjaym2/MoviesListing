package sample.movies.listing;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import sample.movies.listing.data.MovieItem;
import sample.movies.listing.databinding.MovieItemBinding;
import sample.movies.listing.log.AppLog;
import sample.movies.listing.pojo.IndexWithBitmap;
import sample.movies.listing.util.FileUtils;
import sample.movies.listing.util.TiffFileReader;

class MoviesRecyclerAdapter
    extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieItemViewHolder> {
  private final List<MovieItem> movieList;
  private final Context context;
  private final int imageWidth;
  private final int imageHeight;
  private final TiffFileReader tiffFileReader;
  private final String logTag = this.getClass().getSimpleName();
  private WeakReference<MoviesListingActivity> activityReference;
  //to limit number of simultaneously running threads
  private ExecutorService bitmapRequestThreadPool = Executors.newFixedThreadPool(1);

  MoviesRecyclerAdapter(List<MovieItem> movieList, Context context, int imageWidth) {
    this.movieList = movieList;
    this.context = context;
    this.imageWidth = imageWidth;
    imageHeight = (int) (imageWidth * 0.94); // 4828 / 5181 = 0.94
    tiffFileReader = new TiffFileReader();
    activityReference = new WeakReference<>((MoviesListingActivity) context);
  }

  /**
   * Called when RecyclerView needs a new {@link MovieItemViewHolder} of the given type to represent
   * an item.
   * <p>
   * This new ViewHolder should be constructed with a new View that can represent the items
   * of the given type. You can either create a new View manually or inflate it from an XML
   * layout file.
   * <p>
   * The new ViewHolder will be used to display items of the adapter using
   * {@link #onBindViewHolder(MovieItemViewHolder, int, List)}. Since it will be re-used to display
   * different items in the data set, it is a good idea to cache references to sub views of
   * the View to avoid unnecessary {@link View#findViewById(int)} calls.
   *
   * @param parent The ViewGroup into which the new View will be added after it is bound to
   * an adapter position.
   * @param viewType The view type of the new View.
   * @return A new ViewHolder that holds a View of the given view type.
   * @see #getItemViewType(int)
   * @see #onBindViewHolder(MovieItemViewHolder, int)
   */
  @NonNull @Override public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    MovieItemBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.movie_item,
            parent, false);
    return new MovieItemViewHolder(binding);
  }

  /**
   * Called by RecyclerView to display the data at the specified position. This method should
   * update the contents of the {@link MovieItemViewHolder#itemView} to reflect the item at the
   * given
   * position.
   * <p>
   * Note that unlike {@link ListView}, RecyclerView will not call this method
   * again if the position of the item changes in the data set unless the item itself is
   * invalidated or the new position cannot be determined. For this reason, you should only
   * use the <code>position</code> parameter while acquiring the related data item inside
   * this method and should not keep a copy of it. If you need the position of an item later
   * on (e.g. in a click listener), use {@link MovieItemViewHolder#getAdapterPosition()} which will
   * have the updated adapter position.
   *
   * Override {@link #onBindViewHolder(MovieItemViewHolder, int, List)} instead if Adapter can
   * handle efficient partial bind.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the
   * item at the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
    final MovieItem movieItem = movieList.get(position);
    ConstraintSet constraintSet = new ConstraintSet();
    //constraintSet.connect(R.id.nameTextView, ConstraintSet.BOTTOM, R.id.posterImageView,
    //    ConstraintSet.TOP);
    constraintSet.connect(R.id.posterImageView, ConstraintSet.TOP, R.id.nameTextView,
        ConstraintSet.BOTTOM/*, 0*/);
    //constraintSet.connect(R.id.posterImageView, ConstraintSet.START, ConstraintSet.PARENT_ID,
    //    ConstraintSet.START);
    //constraintSet.connect(R.id.posterImageView, ConstraintSet.BOTTOM, R.id.nameTextView,
    //    ConstraintSet.TOP);
    //constraintSet.connect(R.id.posterImageView, ConstraintSet.START, ConstraintSet.PARENT_ID,
    //    ConstraintSet.START);
    //constraintSet.connect(R.id.nameTextView, ConstraintSet.TOP, ConstraintSet.PARENT_ID,
    //    ConstraintSet.TOP);
    constraintSet.applyTo(
        holder.movieItemParentConstraintLayout);//IllegalArgumentException: right to top undefined
    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(imageWidth,
        imageHeight);
    //layoutParams.topMargin = 100;
    //layoutParams.constraintTopToBottomOf()
    holder.posterImageView.setLayoutParams(layoutParams);
    // set a unique tag for each item.
    holder.posterImageView.setTag(position);
    //loadMediaQueue.add(position);
    // clear previous image.
    holder.posterImageView.setImageBitmap(null);
    // read the bitmap asynchronously on non UI thread and later set it in the image view
    fetchImageBitmap(holder, position, movieItem);
    holder.bind(movieItem);
  }

  private void fetchImageBitmap(@NonNull final MovieItemViewHolder holder, final int position,
      final MovieItem movieItem) {

    Runnable fetchImageBitmap = new Runnable() {
      @Override public void run() {
        if (position != (int) holder.posterImageView.getTag()) {
          // if position and tag don't match then wrong bitmap might be loaded, so ignore it.
          AppLog.warn(logTag, "position: " + position + " doesn't match tag:"
              + holder.posterImageView.getTag());
          return;
        }
        Bitmap bitmap = getBitmap(movieItem.getPosterLink(), imageWidth, imageHeight);
        final IndexWithBitmap indexWithBitmap = new IndexWithBitmap(position,
            bitmap);

        if (activityReference != null) {
          activityReference.get().runOnUiThread(new Runnable() {
            @Override public void run() {
              if (indexWithBitmap.getIndexPosition() == (int) holder.posterImageView.getTag()) {
                // correct bitmap is for this position, so we can show it.
                AppLog.verbose(logTag, "position: " + position +
                    " matches the tag of received: " + holder.posterImageView.getTag());
                holder.posterImageView.setImageBitmap(indexWithBitmap.getBitmap());
              } else {
                AppLog.debug(logTag, "Position doesn't match of received" +
                    " indexWithBitmap with current value of holder.posterImageView.getTag()");
              }
            }
          });
        }
      }
    };
    Thread thread = new Thread(fetchImageBitmap);
    // let executor service limit and handle simultaneously running threads.
    bitmapRequestThreadPool.submit(thread);
  }

  private Bitmap getBitmap(String posterLinkUrl, int imageWidth, int imageHeight) {
    Bitmap bitmap;
    // get the bitmap from cache if the image file exists with the same dimensions
    if (FileUtils.isFileCached(posterLinkUrl, imageWidth, imageHeight, context)) {
      bitmap = FileUtils.readBitmapFromCachedFile(posterLinkUrl, imageWidth, imageHeight, context);
    } else {
      bitmap = tiffFileReader.read(getFilePath(posterLinkUrl), imageWidth, imageHeight);
      // save/cache bitmap to a file
      FileUtils.saveBitmapToDiskCache(bitmap, posterLinkUrl, imageWidth, imageHeight, context);
    }
    return bitmap;
  }

  private String getFilePath(String fileNameInAssets) {
    return Objects.requireNonNull(FileUtils.getFileFromAssets(context, fileNameInAssets))
        .getAbsolutePath();
  }

  /**
   * Returns the total number of items in the data set held by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override public int getItemCount() {
    if (movieList != null) {
      return movieList.size();
    }
    return 0;
  }

  static class MovieItemViewHolder extends RecyclerView.ViewHolder {

    final MovieItemBinding itemRowBinding;
    final ConstraintLayout movieItemParentConstraintLayout;
    final ImageView posterImageView;

    MovieItemViewHolder(@NonNull MovieItemBinding itemRowBinding) {
      super(itemRowBinding.getRoot());
      this.itemRowBinding = itemRowBinding;
      movieItemParentConstraintLayout = itemRowBinding.movieItemParentConstraintLayout;
      posterImageView = itemRowBinding.posterImageView;
    }

    void bind(Object object) {
      itemRowBinding.setVariable(BR.movieItem, object);
      itemRowBinding.executePendingBindings();
    }
  }
}