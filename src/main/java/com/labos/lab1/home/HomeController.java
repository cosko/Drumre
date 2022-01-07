package com.labos.lab1.home;

import com.labos.lab1.movie.Movie;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/")
@Controller
public class HomeController {

  @Value("${tmdb-api-key}")
  private String tmdbApiKey;

  @Value("${omdb-api-key}")
  private String omdbApiKey;

  private final Integer noOfItems = 15;

  private List<Movie> popularMovies = new ArrayList<>();
  private List<Movie> topRatedMovies = new ArrayList<>();
  private List<Movie> upcomingMovies = new ArrayList<>();

  @GetMapping
  public String home(Model model) throws UnirestException {

    fetchDefaultMovies();
    model.addAttribute("popularMovies", popularMovies);
    System.out.println(popularMovies);
    model.addAttribute("topRatedMovies", topRatedMovies);
    model.addAttribute("upcomingMovies", upcomingMovies);
    return "index";
  }

  private void fetchDefaultMovies() throws UnirestException {

    HttpResponse<String> responsePopular = Unirest.get("https://api.themoviedb.org/3/movie/popular?api_key="
                    + tmdbApiKey).asString();
    HttpResponse<String> responseTopRated = Unirest.get("https://api.themoviedb.org/3/movie/top_rated?api_key="
                    + tmdbApiKey).asString();
    HttpResponse<String> responseUpcoming = Unirest.get("https://api.themoviedb.org/3/movie/upcoming?api_key="
                    + tmdbApiKey).asString();

    JSONObject jsonPopular = new JSONObject(responsePopular.getBody());
    JSONArray resultsPopular = jsonPopular.getJSONArray("results");
    JSONObject jsonTopRated = new JSONObject(responseTopRated.getBody());
    JSONArray resultsTopRated = jsonTopRated.getJSONArray("results");
    JSONObject jsonUpcoming = new JSONObject(responseUpcoming.getBody());
    JSONArray resultsUpcoming = jsonUpcoming.getJSONArray("results");
    for (int i = 0; i < noOfItems; i++){
      String imdbIdPopular = getImdbId(String.valueOf(resultsPopular.getJSONObject(i).getInt("id")));
      String imdbIdTopRated = getImdbId(String.valueOf(resultsTopRated.getJSONObject(i).getInt("id")));
      String imdbIdUpcoming = getImdbId(String.valueOf(resultsUpcoming.getJSONObject(i).getInt("id")));
      popularMovies.add(getMovieById(imdbIdPopular));
      topRatedMovies.add(getMovieById(imdbIdTopRated));
      upcomingMovies.add(getMovieById(imdbIdUpcoming));
    }
  }

  private String getImdbId(String tmdbId) throws UnirestException {
    HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/" + tmdbId +
                    "/external_ids?api_key=" + tmdbApiKey)
            .asString();

    JSONObject jsonObject = new JSONObject(response.getBody());
    return jsonObject.getString("imdb_id");
  }

  private Movie getMovieById(String imdbId) throws UnirestException {

    HttpResponse<String> response = Unirest.get("http://www.omdbapi.com/?apikey=" + omdbApiKey + "&i=" + imdbId)
            .asString();
    JSONObject jsonObject = new JSONObject(response.getBody());
    Movie movie = new Movie();
    movie.setMovieParams(jsonObject);

    return movie;
  }
}
