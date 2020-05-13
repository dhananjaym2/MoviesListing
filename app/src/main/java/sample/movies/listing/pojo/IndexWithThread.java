package sample.movies.listing.pojo;

/**
 * Holds a reference to the thread with an index position
 */
public class IndexWithThread {
  private int index;
  private Thread thread;

  public IndexWithThread(int index, Thread thread) {
    this.index = index;
    this.thread = thread;
  }

  public Thread getThread() {
    return thread;
  }

  public int getIndex() {
    return index;
  }
}