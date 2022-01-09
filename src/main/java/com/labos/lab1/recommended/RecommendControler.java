package com.labos.lab1.recommended;

import java.util.List;

import com.labos.lab1.movie.Movie;
import com.labos.lab1.movie.MovieRepository;
import com.labos.lab1.user.User;
import com.labos.lab1.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecommendControler {

    private final MovieRepository movieRepository;
    private final UserService userService;
    private final RecommendedService recommendedService;

    public RecommendControler(MovieRepository movieRepository, UserService userService,
                              RecommendedService recommendedService) {
        this.movieRepository = movieRepository;
        this.userService = userService;
        this.recommendedService = recommendedService;
    }

    @GetMapping("/recommended")
    public String recommended(Model model, Authentication auth, @AuthenticationPrincipal OAuth2User user){
        User currentUser = userService.getUniqueUser(auth, user);
        model.addAttribute("recommendedMovies",
                           currentUser.getRecommended().stream().findFirst().get().getTitle() == null ? movieRepository.findBestMovies().subList(0, 50)
                                                                  : currentUser.getRecommended().subList(0, 100));
        return "pages/recommended";
    }

    @PostMapping("/recommended")
    public String getRecommendations(Authentication auth, Model model, @AuthenticationPrincipal OAuth2User user){
        User currentUser = userService.getUniqueUser(auth, user);
        List<Movie> recommendedMovies = recommendedService.findRecommendedMovies(currentUser.getGenres(), currentUser.getActors(), currentUser);

        if (recommendedMovies.isEmpty()){
            model.addAttribute("recommendedMovies", movieRepository.findBestMovies().subList(0, 50));
        } else{
            model.addAttribute("recommendedMovies", recommendedMovies.subList(0, 100));
        }
        return "pages/recommended";
    }
}
