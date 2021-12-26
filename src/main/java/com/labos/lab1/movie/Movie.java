package com.labos.lab1.movie;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("movies")
public class Movie {

  private String name;
  private String summary;
  private String premiered;
  private Rating rating;
  private Picture image;

  public Movie(String name, String summary, String premiered, Rating rating, Picture image) {
    this.name = name;
    this.summary = summary;
    this.premiered = premiered;
    this.rating = rating;
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getPremiered() {
    return premiered;
  }

  public void setPremiered(String premiered) {
    this.premiered = premiered;
  }

  public Rating getRating() {
    return rating;
  }

  public void setRating(Rating rating) {
    this.rating = rating;
  }

  public Picture getImage() {
    return image;
  }

  public void setImage(Picture image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "Movie{" +
        "name='" + name + '\'' +
        ", summary='" + summary + '\'' +
        ", premiered='" + premiered + '\'' +
        ", rating=" + rating +
        ", image=" + image +
        '}';
  }
}
