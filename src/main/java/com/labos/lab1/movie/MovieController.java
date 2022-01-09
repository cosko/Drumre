package com.labos.lab1.movie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movies")
public class MovieController {

  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping
  public String getMovieInfo(Model model) {
    model.addAttribute("movies", movieService.findAllMovies());
    model.addAttribute("movieFilter", new MovieFilter());
    return "pages/movies";
  }

  @PostMapping
  public String findMovies(Model model, MovieFilter filter) {
    model.addAttribute("movies", movieService.findFilteredMovies(filter));
    model.addAttribute("movieFilter", filter);
    return "pages/movies";
  }

}
