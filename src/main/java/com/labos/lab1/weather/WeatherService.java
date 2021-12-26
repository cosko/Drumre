package com.labos.lab1.weather;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

  private WeatherRepository weatherRepository;

  public WeatherService(WeatherRepository weatherRepository) {
    this.weatherRepository = weatherRepository;
  }

  public Weather save(String data) throws JsonProcessingException {
    Gson gson = new Gson();
    Weather weather = gson.fromJson(data, Weather.class);
    return weatherRepository.save(weather);
  }
}
