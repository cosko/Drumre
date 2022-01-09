package com.labos.lab1.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labos.lab1.movie.Movie;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {

  @Id
  private String id;
  private String username;
  private Long twitterId;
  private Long facebookId;
  private Map<String, Integer> watched;
  private Map<String, Integer> actors;
  private Map<String, Integer> genres;
  private List<Movie> recommended;
  private String firstName;
  private String lastName;
  private String picture;
  private String email;
  private Boolean emailVerified;

  public User(String firstName, String lastName, String picture, String email,
              Boolean emailVerified) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.picture = picture;
    this.email = email;
    this.emailVerified = emailVerified;
  }

  public User(String username, Long twitterId, String picture) {
    this.username = username;
    this.twitterId = twitterId;
    this.picture = picture;
  }

  public User() {
  }

  public Map<String, Integer> getActors() {
    if(actors == null)
        actors = new HashMap<String, Integer>();
    return actors;
  }

  public void setActors(Map<String, Integer> actors) {
    this.actors = actors;
  }

  public void setActorEntry(String actor, Integer score) {
    this.actors.put(actor, score);
  }

  public Map<String, Integer> getGenres() {
      if(genres == null)
          genres = new HashMap<String, Integer>();
      return genres;
  }

  public void setGenres(Map<String, Integer> genres) {
    this.genres = genres;
  }

  public void setGenreEntry(String genre, Integer score) {
    this.genres.put(genre, score);
  }

  public Long getTwitterId() {
    return twitterId;
  }

  public void setTwitterId(Long twitterId) {
    this.twitterId = twitterId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Map<String, Integer> getWatched() {
      if(watched == null)
          watched = new HashMap<String, Integer>();
      return watched;
  }

  public void setWatched(Map<String, Integer> watched) {
    this.watched = watched;
  }

  public void setWatchedEntry(String movie, Integer score) {
    this.watched.put(movie, score);
  }
  public void setActorsEntry(String movie, Integer score) {
    this.actors.put(movie, score);
  }

  public List<Movie> getRecommended() {
    return recommended;
  }

  public void setRecommended(List<Movie> recommended) {
    this.recommended = recommended;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean isEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public Long getFacebookId() {
    return facebookId;
  }

  public void setFacebookId(Long facebookId) {
    this.facebookId = facebookId;
  }

}
