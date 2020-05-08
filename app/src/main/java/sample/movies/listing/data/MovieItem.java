package sample.movies.listing.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for each data of movie item to be displayed in the list.
 */
public class MovieItem implements Parcelable {

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

  public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
    @Override
    public MovieItem createFromParcel(Parcel in) {
      return new MovieItem(in);
    }

    @Override
    public MovieItem[] newArray(int size) {
      return new MovieItem[size];
    }
  };

  MovieItem(String name, String payment_plan, int release_year, String
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

  protected MovieItem(Parcel in) {
    name = in.readString();
    payment_plan = in.readString();
    release_year = in.readInt();
    video_duration = in.readString();
    type = in.readString();
    created_on = in.readString();
    updated_on = in.readString();
    posterLink = in.readString();
    shortDescription = in.readString();
    description = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(payment_plan);
    dest.writeInt(release_year);
    dest.writeString(video_duration);
    dest.writeString(type);
    dest.writeString(created_on);
    dest.writeString(updated_on);
    dest.writeString(posterLink);
    dest.writeString(shortDescription);
    dest.writeString(description);
  }

  @Override
  public int describeContents() {
    return 0;
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
