package com.labos.lab1.recommended;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.labos.lab1.movie.Movie;
import com.labos.lab1.movie.MovieRepository;
import com.labos.lab1.user.User;
import com.labos.lab1.user.UserRepository;
import com.labos.lab1.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RecommendControler {
    
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/recommended")
    public String recommended(Authentication auth, Model model, @AuthenticationPrincipal OAuth2User user, HttpServletRequest request){
        
        List<Movie> bestMovies = movieRepository.findBestMovies();
        List<Movie> popularMovies = movieRepository.findPopularMovies();
        model.addAttribute("topMovies", bestMovies.subList(0, 10));
        model.addAttribute("popularMovies", popularMovies.subList(0, 10));

        return "pages/recommended";
    }

    @PostMapping("/recommended")
    public ModelAndView getRecommendations(Authentication auth, Model model, @AuthenticationPrincipal OAuth2User user, HttpServletRequest request){

        User currentUser = userService.getUniqueUser(auth, user);
        for(Movie movie : movieRepository.findBestMovies()){
            for(String actor : Arrays.asList(movie.getActors().split(", "))){
                if (currentUser.getActors().containsKey(actor)){
                }
            }
        }

        return new ModelAndView("redirect:/recommended");
    }
}
