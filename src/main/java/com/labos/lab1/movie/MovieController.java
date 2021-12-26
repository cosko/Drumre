package com.labos.lab1.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/movies")
public class MovieController {

  private MovieService movieService;
  private RestTemplate restTemplate;

  public MovieController(MovieService movieService, RestTemplate restTemplate){
    this.movieService = movieService;
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public String getMovieInfo(Model model) throws ParseException, JsonProcessingException {
    if (movieService.collectionSize() == 0){
      fetchMovies();
    }
    model.addAttribute("movies", movieService.getAll());
    return "pages/movies";
  }

  private void fetchMovies() {
    String baseUrl = "https://api.tvmaze.com/shows/";
    for(int i = 1; i < 100; i++){
      String url = baseUrl + String.valueOf(i);
      try{
        String res = restTemplate.getForObject(url, String.class);
        movieService.save(res);
      } catch (Exception e){
        System.out.println(e.getMessage());
      }
    }
  }

}
