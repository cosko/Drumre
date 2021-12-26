package com.labos.lab1.weather;

public class WeatherData {

  private long id;
  private String main;
  private String description;

  public WeatherData(long id, String main, String description) {
    this.id = id;
    this.main = main;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getMain() {
    return main;
  }

  public void setMain(String main) {
    this.main = main;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
