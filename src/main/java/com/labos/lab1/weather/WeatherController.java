package com.labos.lab1.weather;

import java.lang.System;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/weather")
public class WeatherController {

  @Value("${openweather-api-key}")
  private String apiKey;

  private WeatherService weatherService;
  private RestTemplate restTemplate;

  public WeatherController(WeatherService weatherService, RestTemplate restTemplate){
    this.weatherService = weatherService;
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public String getWeather(Model model,
                           @RequestParam(value = "city", required = false) String city){
    if(city != null){
      try {
        String url="https://api.openweathermap.org/data/2.5/weather?q="+ city + "&appid=" + apiKey + "&units=metric";
        String res = restTemplate.getForObject(url, String.class);
        Weather weather = weatherService.save(res);
        model.addAttribute("weather", weather);
      } catch (Exception e) {
        model.addAttribute("message", "Nema podataka za navedeni grad");
        System.out.println(e.getMessage());
      }
    }
    return "pages/weather";
  }
}
