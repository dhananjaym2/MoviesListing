package sample.movies.listing.util;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import sample.movies.listing.errorHandling.AppErrorHandler;
import sample.movies.listing.log.AppLog;

public class FileUtils {
  private static final String logTag = FileUtils.class.getSimpleName();

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

  public static File getFileFromAssets(Context context, String fileName) {
    AssetManager am = context.getAssets();
    InputStream inputStream = null;
    try {
      inputStream = am.open("img/" + "TestImage.tif");// same file is used TestImage.tif
      //context.getExternalFilesDir(null)
      return createFileFromInputStream(context.getFilesDir(), fileName, inputStream);
    } catch (IOException e) {
      new AppErrorHandler(e);
    }
    return null;
  }

  private static File createFileFromInputStream(File filesDir, String fileName,
      InputStream inputStream) {

    try {
      File f = new File(filesDir + "/" + fileName);
      if (!f.exists()) {
        OutputStream outputStream = new FileOutputStream(f);
        byte[] buffer = new byte[1024];
        int length = 0;

        while ((length = inputStream.read(buffer)) > 0) {
          outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();
      }
      AppLog.debug(logTag, "File path: " + f.getAbsolutePath());

      return f;
    } catch (IOException e) {
      //Logging exception
      new AppErrorHandler(e);
    }

    return null;
  }
}
