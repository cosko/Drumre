package com.labos.lab1.user;

import java.util.List;
import java.util.Map;

import com.labos.lab1.movie.Movie;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

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
  private List<Pair<Movie, Double>> recommended;
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
    return actors;
  }

  public void setActors(Map<String, Integer> actors) {
    this.actors = actors;
  }

  public Map<String, Integer> getGenres() {
    return genres;
  }

  public void setGenres(Map<String, Integer> genres) {
    this.genres = genres;
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
    return watched;
  }

  public void setWatched(Map<String, Integer> watched) {
    this.watched = watched;
  }

  public List<Pair<Movie, Double>> getRecommended() {
    return recommended;
  }

  public void setRecommended(
      List<Pair<Movie, Double>> recommended) {
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
