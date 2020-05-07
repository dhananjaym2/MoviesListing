package sample.movies.listing.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
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
    InputStream inputStream;
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
    // TODO Check memory space before saving.
    try {
      File f = new File(filesDir + "/" + fileName);
      if (!f.exists()) {
        OutputStream outputStream = new FileOutputStream(f);
        byte[] buffer = new byte[1024];
        int length;

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

  public static void saveBitmapToDiskCache(Bitmap bitmap, String fileName, int imageWidth,
      int imageHeight, Context context) {
    String cacheFilePath = getCacheFilePathString(fileName, imageWidth, imageHeight, context);
    saveBitmapToFile(new File(cacheFilePath), bitmap);
  }

  private static String getCacheFilePathString(@NonNull String fileName, int imageWidth,
      int imageHeight,
      @NonNull Context context) {
    return context.getCacheDir().getPath() + "/" + imageWidth + "/" + imageHeight + "/" + fileName;
  }

  private static void saveBitmapToFile(File file, Bitmap bitmap) {
    FileOutputStream out = null;
    try {
      if (!file.exists()) {
        file.mkdirs();
      }
      file.delete();
      out = new FileOutputStream(file);
      // Use the compress method on the BitMap object to write image to the OutputStream
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
      out.flush();
    } catch (IOException e) {
      new AppErrorHandler(e);
    } finally {
      try {
        if (out != null) out.close();
      } catch (IOException e) {
        new AppErrorHandler(e);
      }
    }
  }

  public static boolean isFileCached(String fileName, int imageWidth, int imageHeight,
      Context context) {
    if (fileName == null || context == null) {
      AppLog.error(logTag, "fileName or context is null");
      return false;
    }
    String cacheFilePath = getCacheFilePathString(fileName, imageWidth, imageHeight, context);
    File cachedFile = new File(cacheFilePath);

    return cachedFile.exists();
  }

  public static Bitmap readBitmapFromCachedFile(String fileName, int imageWidth,
      int imageHeight, Context context) {
    String photoFilePath = getCacheFilePathString(fileName, imageWidth, imageHeight, context);
    if (new File(photoFilePath).exists()) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inPreferredConfig = Bitmap.Config.ALPHA_8;
      return BitmapFactory.decodeFile(photoFilePath, options);
    } else {
      AppLog.error(logTag, "photoFilePath doesn't exist:" + photoFilePath);
      return null;
    }
  }
}
