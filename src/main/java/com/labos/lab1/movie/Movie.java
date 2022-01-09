package com.labos.lab1.movie;

import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("movies")
public class Movie {

  private String title;
  private String year;
  private String runtime;
  private String genre;
  private String director;
  private String actors;
  private String plot;
  private String poster;
  private String imdbRating;
  private String uniqueId;

  public void setMovieParams(JSONObject jsonObject){
    this.setTitle(jsonObject.getString("Title"));
    this.setYear(jsonObject.getString("Year"));
    this.setRuntime(jsonObject.getString("Runtime"));
    this.setGenre(jsonObject.getString("Genre"));
    this.setDirector(jsonObject.getString("Director"));
    this.setActors(jsonObject.getString("Actors"));
    this.setPlot(jsonObject.getString("Plot"));
    this.setPoster(jsonObject.getString("Poster"));
    this.setImdbRating(jsonObject.getString("imdbRating"));
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getRuntime() {
    return runtime;
  }

  public void setRuntime(String runtime) {
    this.runtime = runtime;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getActors() {
    return actors;
  }

  public void setActors(String actors) {
    this.actors = actors;
  }

  public String getPlot() {
    return plot;
  }

  public void setPlot(String plot) {
    this.plot = plot;
  }

  public String getPoster() {
    return poster;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  public String getImdbRating() {
    return imdbRating;
  }

  public void setImdbRating(String imdbRating) {
    this.imdbRating = imdbRating;
  }

  @Override
  public String toString() {
    return "Movie{" +
            "title='" + title + '\'' +
            ", year='" + year + '\'' +
            ", runtime='" + runtime + '\'' +
            ", genre='" + genre + '\'' +
            ", director='" + director + '\'' +
            ", actors='" + actors + '\'' +
            ", plot='" + plot + '\'' +
            ", poster='" + poster + '\'' +
            ", imdbRating='" + imdbRating + '\'' +
            ", uniqueId='" + uniqueId + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Movie movie = (Movie) o;

    return uniqueId != null ? uniqueId.equals(movie.uniqueId) : movie.uniqueId == null;
  }

  @Override
  public int hashCode() {
    return uniqueId != null ? uniqueId.hashCode() : 0;
  }
}
