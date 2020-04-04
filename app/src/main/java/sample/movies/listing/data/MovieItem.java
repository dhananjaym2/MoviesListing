package sample.movies.listing.data;

/**
 * Model for each data of movie item to be displayed in the list.
 */
public class MovieItem {

  private int id;

  private String name;

  private String payment_plan;

  private int release_year;

  private String video_duration;

  private String type;

  private String created_on;

  private String updated_on;

  private String posterLink;

  private String shortDescription;

  private String description;

  public MovieItem(int id, String name, String payment_plan, int release_year, String
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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPayment_plan() {
    return payment_plan;
  }

  public void setPayment_plan(String payment_plan) {
    this.payment_plan = payment_plan;
  }

  public int getRelease_year() {
    return release_year;
  }

  public void setRelease_year(int release_year) {
    this.release_year = release_year;
  }

  public String getVideo_duration() {
    return video_duration;
  }

  public void setVideo_duration(String video_duration) {
    this.video_duration = video_duration;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCreated_on() {
    return created_on;
  }

  public void setCreated_on(String created_on) {
    this.created_on = created_on;
  }

  public String getUpdated_on() {
    return updated_on;
  }

  public void setUpdated_on(String updated_on) {
    this.updated_on = updated_on;
  }

  public String getPosterLink() {
    return posterLink;
  }

  public void setPosterLink(String posterLink) {
    this.posterLink = posterLink;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
