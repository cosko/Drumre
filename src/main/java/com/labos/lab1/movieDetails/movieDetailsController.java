package com.labos.lab1.movieDetails;

import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class movieDetailsController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @GetMapping("/movieDetails/{id}")
    public String movieDetails(Authentication auth, Model model, @PathVariable("id") String id,
            @AuthenticationPrincipal OAuth2User user, HttpServletRequest request) {
        User currentUser = userService.getUniqueUser(auth, user);

        if (id == "" || id == null) {
            return "index";
        }

        Movie movie = movieRepository.findByUniqueId(id);
        model.addAttribute("movie", movie);
        request.getSession().setAttribute("movie", movie.getUniqueId());
        ;
        if (currentUser.getWatched() == null) {
            model.addAttribute("autoselect", 0);
            return "pages/movieDetails";
        }
        if (currentUser.getWatched().containsKey(movie.getUniqueId())) {
            model.addAttribute("autoselect", 1);
            model.addAttribute("rating", currentUser.getWatched().get(movie.getUniqueId()));
        } else {
            model.addAttribute("autoselect", 0);
        }

        return "pages/movieDetails";
    }

    @PostMapping("/movieDetails")
    public ModelAndView updateUser(Authentication auth, Model model, @RequestParam("watched-status") Integer updateUser,
            @AuthenticationPrincipal OAuth2User user, HttpServletRequest request) {
        User userObject;
        if (user == null) {
            userObject = ((User) auth.getPrincipal());
            userObject = userRepository.findByTwitterId(userObject.getTwitterId()).get();
        } else {
            userObject = userRepository.findByEmail(user.getAttribute("email")).get();
        }

        String uniqueId = (String) request.getSession().getAttribute("movie");
        if (updateUser == 0) {
            if (userObject.getWatched() != null && userObject.getWatched().containsKey(uniqueId)) {
                if (userObject.getGenres() != null) {
                    List<String> genres = Arrays
                            .asList(movieRepository.findByUniqueId(uniqueId).getGenre().split(", "));
                    Integer scoreChange;
                    for (String genre : genres) {
                        if (userObject.getGenres().get(genre) != null) {
                            scoreChange = userObject.getGenres().get(genre)
                                    + (userObject.getWatched().get(uniqueId) - userObject.getGenres().get(genre)) / 2;
                        } else {
                            scoreChange = userObject.getGenres().get(genre);
                        }
                        userObject.getGenres().put(genre, scoreChange);
                    }
                }
                if (userObject.getActors() != null) {
                    List<String> actors = Arrays
                            .asList(movieRepository.findByUniqueId(uniqueId).getActors().split(", "));
                    Integer scoreChangeActor;
                    for (String actor : actors) {
                        actor = String.join("", Arrays.asList(actor.split("\\.")));
                        if (userObject.getGenres().get(actor) != null) {
                            scoreChangeActor = userObject.getGenres().get(actor)
                                    + (userObject.getWatched().get(uniqueId) - userObject.getGenres().get(actor)) / 2;
                        } else {
                            scoreChangeActor = userObject.getGenres().get(actor);
                        }
                        userObject.getActors().put(actor, scoreChangeActor);
                    }
                }
                userObject.getWatched().remove(uniqueId);
            }
        } else if (updateUser == 1) {
            if (userObject.getWatched() == null) {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put(uniqueId, 5);
                userObject.setWatched(map);
            } else {
                userObject.getWatched().put(uniqueId, 5);

            }
            if (userObject.getActors() == null) {
                HashMap<String, Integer> actorsMap = new HashMap<>();
                userObject.setActors(actorsMap);
            }
            if (userObject.getGenres() == null) {
                HashMap<String, Integer> genresMap = new HashMap<>();
                userObject.setGenres(genresMap);
            }
            List<String> genres = Arrays.asList(movieRepository.findByUniqueId(uniqueId).getGenre().split(", "));
            Integer scoreChange;
            for (String genre : genres) {
                if (userObject.getGenres().get(genre) != null) {
                    scoreChange = userObject.getGenres().get(genre)
                            + (userObject.getWatched().get(uniqueId) - userObject.getGenres().get(genre)) / 2;
                } else {
                    scoreChange = 5;
                }
                userObject.getGenres().put(genre, scoreChange);
            }
            List<String> actors = Arrays.asList(movieRepository.findByUniqueId(uniqueId).getActors().split(", "));
            Integer scoreChangeActor;
            for (String actor : actors) {
                actor = String.join("", Arrays.asList(actor.split("\\.")));
                if (userObject.getActors().get(actor) != null) {
                    scoreChangeActor = userObject.getActors().get(actor)
                            + (userObject.getWatched().get(uniqueId) - userObject.getActors().get(actor)) / 2;
                } else {
                    scoreChangeActor = 5;
                }
                scoreChangeActor = userObject.getActors().put(actor, scoreChangeActor);
            }
        }
        userRepository.save(userObject);
        return new ModelAndView("redirect:/movieDetails/" + uniqueId);
    }

    @PostMapping("/updateRating")
    public ModelAndView updateRating(Authentication auth, Model model, @RequestParam("rating") Integer rating,
            @AuthenticationPrincipal OAuth2User user, HttpServletRequest request) {
        User userObject;
        if (user == null) {
            userObject = ((User) auth.getPrincipal());
            userObject = userRepository.findByTwitterId(userObject.getTwitterId()).get();
        } else {
            userObject = userRepository.findByEmail(user.getAttribute("email")).get();
        }

        String uniqueId = (String) request.getSession().getAttribute("movie");
        userObject.getWatched().put(uniqueId, rating);

        List<String> genres = Arrays.asList(movieRepository.findByUniqueId(uniqueId).getGenre().split(", "));
        Integer scoreChange;
        for (String genre : genres) {
            if (userObject.getGenres().get(genre) != null) {
                scoreChange = userObject.getGenres().get(genre)
                        + (userObject.getWatched().get(uniqueId) - userObject.getGenres().get(genre)) / 2;
            } else {
                scoreChange = rating;
            }
            userObject.getGenres().put(genre, scoreChange);
        }
        List<String> actors = Arrays.asList(movieRepository.findByUniqueId(uniqueId).getActors().split(", "));
        Integer scoreChangeActor;
        for (String actor : actors) {
            actor = String.join("", Arrays.asList(actor.split("\\.")));
            if (userObject.getActors().get(actor) != null) {
                scoreChangeActor = userObject.getActors().get(actor)
                        + (userObject.getWatched().get(uniqueId) - userObject.getActors().get(actor)) / 2;
            } else {
                scoreChangeActor = rating;
            }
            scoreChangeActor = userObject.getActors().put(actor, scoreChangeActor);
        }
        
        userRepository.save(userObject);

        return new ModelAndView("redirect:/movieDetails/" + uniqueId);
    }
}
