package com.labos.lab1.recommended;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                           currentUser.getRecommended() == null ? movieRepository.findBestMovies().subList(0, 50)
                                                                  : currentUser.getRecommended());
        return "pages/recommended";
    }

    @PostMapping("/recommended")
    public String getRecommendations(Authentication auth, Model model, @AuthenticationPrincipal OAuth2User user){
        User currentUser = userService.getUniqueUser(auth, user);
        Map<String, Integer> genres = generateGenres();
        Map<String, Integer> actors = generateActors();

        List<Movie> recommendedMovies = recommendedService.findRecommendedMovies(genres, actors, currentUser);
        if (recommendedMovies.isEmpty()){
            model.addAttribute("recommendedMovies", movieRepository.findBestMovies().subList(0, 50));
        } else{
            model.addAttribute("recommendedMovies", recommendedMovies.subList(0, 100));
        }
        return "pages/recommended";
    }

    private Map<String, Integer> generateActors() {
        Map<String, Integer> mapa = new HashMap<>();
        mapa.put("Matthew McConaughey", 7);
        mapa.put("Robert Downey Jr", 9);
        mapa.put("Arnold Schwarzenegger", 6);
        mapa.put("Chris Pratt", -3);
        return mapa;
    }

    private Map<String, Integer> generateGenres() {
        Map<String, Integer> mapa = new HashMap<>();
        mapa.put("Action", 7);
        mapa.put("Adventure", 9);
        mapa.put("Sci-Fi", 6);
        mapa.put("Drama", -3);
        return mapa;
    }
}
