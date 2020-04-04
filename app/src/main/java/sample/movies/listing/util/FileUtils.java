package sample.movies.listing.util;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class FileUtils {
  public static String readFileFromAssetsAsAString(Context context, String filePathInAssets,
      Charset charset) throws IOException {
    String fileContentAsString;

    try (InputStream inputStream = context.getAssets().open(filePathInAssets)) {
      int size = inputStream.available();
      byte[] buffer = new byte[size];
      inputStream.read(buffer);
      fileContentAsString = new String(buffer, charset);
    }
    return fileContentAsString;
  }
}
