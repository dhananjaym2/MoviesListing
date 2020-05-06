package sample.movies.listing.pojo;

import android.graphics.Bitmap;

public class IndexWithBitmap {

  private final int indexPosition;
  private final Bitmap bitmap;

  public IndexWithBitmap(int indexPosition, Bitmap bitmap) {
    this.indexPosition = indexPosition;
    this.bitmap = bitmap;
  }

  public int getIndexPosition() {
    return indexPosition;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }

  //@Override public boolean equals(@Nullable Object obj) {
  //  return (obj instanceof PositionWithBitmap)
  //      && ((PositionWithBitmap) obj).position == this.position;
  //}
}