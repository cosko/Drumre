package com.labos.lab1.movie;

import java.util.List;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import net.minidev.json.parser.ParseException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/movies")
public class MovieController {

  @Value("${tmdb-api-key}")
  private String tmdbApiKey;

  @Value("${omdb-api-key}")
  private String omdbApiKey;

  private MovieService movieService;
  private RestTemplate restTemplate;
  @Autowired
  MovieRepository movieRepository;

  public MovieController(MovieService movieService, RestTemplate restTemplate){
    this.movieService = movieService;
    this.restTemplate = restTemplate;
  }

  @GetMapping
  public String getMovieInfo(Model model, HttpServletRequest request) throws ParseException, JsonProcessingException {
    if (movieService.collectionSize() == 0){
      fetchMovies();
    }
    List<Movie> exampleMovies = movieRepository.findBestMovies();
    //model.addAttribute("movies", exampleMovies);
    model.addAttribute("topMovies", exampleMovies.subList(0, 10));
    List<Movie> popularMovies = movieRepository.findPopularMovies();
    model.addAttribute("popularMovies", popularMovies.subList(0, 10));
    List<Movie> displayedMovies = (List<Movie>)request.getSession().getAttribute("movies");
    model.addAttribute("movies", displayedMovies);
    model.addAttribute("previousInput", request.getSession().getAttribute("input"));

    return "pages/movies";
  }

  @PostMapping
  public ModelAndView findMovies(Model model, @RequestParam("search") String title, HttpServletRequest request) {
    List<Movie> matchingMovies = movieRepository.findByTitleRegex(title);
    request.getSession().setAttribute("movies", matchingMovies);
    request.getSession().setAttribute("input", title);
    return new ModelAndView("redirect:/movies");
  }

  private void fetchMovies() {
    int j = 0;
    for (int i = 0; i < 1000000000; i++){
      try{
      String imdbId = getImdbId(String.valueOf(i));
      j++;
      System.out.println(j);
      if(j <= 905){
        continue;
      }
      HttpResponse<String> response = Unirest.get("http://www.omdbapi.com/?apikey=" + omdbApiKey + "&i=" + imdbId).asString();
      JSONObject movieJson = new JSONObject(response.getBody());
      Movie movie = new Movie();
      movie.setUniqueId(String.valueOf(i));
      movie.setMovieParams(movieJson);
      movieRepository.save(movie);
      } catch(Exception e){
        System.out.println(e.getMessage());
      }
    }
  }
  private String getImdbId(String tmdbId) throws UnirestException {
    HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/" + tmdbId +
                    "/external_ids?api_key=" + tmdbApiKey)
            .asString();
    JSONObject jsonObject = new JSONObject(response.getBody());
    System.out.println(jsonObject.getString("imdb_id"));
    return jsonObject.getString("imdb_id");
  }

}
