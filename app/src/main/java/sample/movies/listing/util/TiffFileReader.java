package sample.movies.listing.util;

import android.graphics.Bitmap;
import java.io.File;
import org.beyka.tiffbitmapfactory.TiffBitmapFactory;
import sample.movies.listing.log.AppLog;

public class TiffFileReader {

  private String logTag = this.getClass().getSimpleName();

  public Bitmap read(String filePath, int requiredWidth, int requiredHeight) {
    File file = new File(filePath);

    //Read data about image to Options object
    TiffBitmapFactory.Options options = new TiffBitmapFactory.Options();
    options.inJustDecodeBounds = true;
    TiffBitmapFactory.decodeFile(file, options);

    int dirCount = options.outDirectoryCount;

    //Read and process all images in file
    for (int i = 0; i < dirCount; i++) {
      options.inDirectoryNumber = i;
      TiffBitmapFactory.decodeFile(file, options);
      int width = options.outWidth;
      int height = options.outHeight;
      //Change sample size if width or height bigger than required width or height
      int inSampleSize = 1;
      if (height > requiredHeight || width > requiredWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > requiredHeight
            && (halfWidth / inSampleSize) > requiredWidth) {
          inSampleSize *= 2;
        }
      }
      options.inJustDecodeBounds = false;
      options.inSampleSize = inSampleSize;

      // Specify the amount of memory available for the final bitmap and temporary storage.
      options.inAvailableMemory = 20000000; // bytes
      AppLog.debug(logTag, "inAvailableMemory:" + options.inAvailableMemory);

      return TiffBitmapFactory.decodeFile(file, options);
    }
    return null;
  }
}
