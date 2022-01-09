package com.labos.lab1.movie;

public class MovieFilter {

  private String title;
  private String genre;
  private String actors;

  public MovieFilter() {
    this.actors = "";
    this.title = "";
    this.genre = "";
  }

  public MovieFilter(String title, String genre, String actors) {
    this.title = title;
    this.genre = genre;
    this.actors = actors;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getActors() {
    return actors;
  }

  public void setActors(String actors) {
    this.actors = actors;
  }
}
