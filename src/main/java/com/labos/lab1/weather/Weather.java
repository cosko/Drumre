package com.labos.lab1.weather;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("weather")
public class Weather {

  private List<WeatherData> weather;
  private Long visibility;
  private Main main;
  private Wind wind;
  private System sys;

  public Weather(List<WeatherData> weather, Long visibility, Main main, Wind wind,
                 System sys) {
    this.weather = weather;
    this.visibility = visibility;
    this.main = main;
    this.wind = wind;
    this.sys = sys;
  }

  public List<WeatherData> getWeather() {
    return weather;
  }

  public void setWeather(List<WeatherData> weather) {
    this.weather = weather;
  }

  public Long getVisibility() {
    return visibility;
  }

  public void setVisibility(Long visibility) {
    this.visibility = visibility;
  }

  public Main getMain() {
    return main;
  }

  public void setMain(Main main) {
    this.main = main;
  }

  public Wind getWind() {
    return wind;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }

  public System getSys() {
    return sys;
  }

  public void setSys(System sys) {
    this.sys = sys;
  }
}
