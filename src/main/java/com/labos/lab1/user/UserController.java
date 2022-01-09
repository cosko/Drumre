package com.labos.lab1.user;

import com.labos.lab1.integration.FacebookService;
import com.labos.lab1.movie.Movie;
import com.labos.lab1.movie.MovieService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class UserController {

  private final UserService userService;

  private final FacebookService fbService;
  private final MovieService movieService;

  public UserController(UserService userService, FacebookService fbService, MovieService movieService){
    this.userService = userService;
    this.fbService = fbService;
    this.movieService = movieService;
  }

  @GetMapping
  public String getUser(Model model, Authentication auth){
    List<Movie> userLikedMovies = fbService.getUserLikedMovies();

    model.addAttribute("user", userService.getUserFromAuth(auth));
    model.addAttribute("likedMovies", userLikedMovies);
    return "pages/profile";
  }
}
