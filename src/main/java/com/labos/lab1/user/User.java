package com.labos.lab1.user;

import java.util.List;
import java.util.Map;

import com.labos.lab1.movie.Movie;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.access.method.P;

@Document("users")
public class User {

  @Id
  private String id;
  private String username;
  private Long twitterId;
  private Map<Movie, Integer> watched;
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

  public Map<Movie, Integer> getWatched() {
    return watched;
  }

  public void setWatched(Map<Movie, Integer> watched) {
    this.watched = watched;
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
}
