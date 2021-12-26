package com.labos.lab1.movie;

public class Picture {

  private String original;
  private String medium;

  public Picture(String original, String medium) {
    this.original = original;
    this.medium = medium;
  }

  public String getOriginal() {
    return original;
  }

  public void setOriginal(String original) {
    this.original = original;
  }

  public String getMedium() {
    return medium;
  }

  public void setMedium(String medium) {
    this.medium = medium;
  }
}
