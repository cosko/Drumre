package com.labos.lab1.weather;

public class System {

  private long id;
  private long sunrise;
  private long sunset;

  public System(long id, long sunrise, long sunset) {
    this.id = id;
    this.sunrise = sunrise;
    this.sunset = sunset;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getSunrise() {
    return sunrise;
  }

  public void setSunrise(long sunrise) {
    this.sunrise = sunrise;
  }

  public long getSunset() {
    return sunset;
  }

  public void setSunset(long sunset) {
    this.sunset = sunset;
  }
}
