package sample.movies.listing.errorHandling;

public class AppErrorHandler {
  public AppErrorHandler(Throwable throwable) {
    throwable.printStackTrace();
  }
}
