package sample.movies.listing.data;

/**
 * Model for each data of movie item to be displayed in the list.
 */
public class MovieItem {

  private final String name;

  private final String payment_plan;

  private final int release_year;

  private final String video_duration;

  private final String type;

  private final String created_on;

  private final String updated_on;

  private final String posterLink;

  private final String shortDescription;

  private final String description;

  public MovieItem(String name, String payment_plan, int release_year, String
      video_duration, String type, String created_on, String updated_on, String posterLink,
      String shortDescription, String description) {
    this.name = name;
    this.payment_plan = payment_plan;
    this.release_year = release_year;
    this.video_duration = video_duration;
    this.type = type;
    this.created_on = created_on;
    this.updated_on = updated_on;
    this.posterLink = posterLink;
    this.shortDescription = shortDescription;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getPayment_plan() {
    return payment_plan;
  }

  public int getRelease_year() {
    return release_year;
  }

  public String getVideo_duration() {
    return video_duration;
  }

  public String getType() {
    return type;
  }

  public String getCreated_on() {
    return created_on;
  }

  public String getUpdated_on() {
    return updated_on;
  }

  public String getPosterLink() {
    return posterLink;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getDescription() {
    return description;
  }
}
