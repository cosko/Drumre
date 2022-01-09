package com.labos.lab1.movieDetails;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    
    @GetMapping("/movieDetails/{title}")
    public String movieDetails(Authentication auth, Model model, @PathVariable("title") String title, @AuthenticationPrincipal OAuth2User user, HttpServletRequest request){
        User currentUser = userService.getUniqueUser(auth, user);
        
        if(title == "" || title == null){
            return "index";
        }
        
        Optional<Movie> movie = movieRepository.findByTitle(title);
        if(movie.isEmpty()){
            model.addAttribute("movie", null);
            //System.out.println("null");
        }
        else{
            model.addAttribute("movie", movie.get());
            request.getSession().setAttribute("movie", movie.get().getTitle());;
            if (currentUser.getWatched() == null){
                model.addAttribute("autoselect", 0);
                return "pages/movieDetails";
            }
            if(currentUser.getWatched().containsKey(movie.get().getTitle())){
                model.addAttribute("autoselect", 1);
                model.addAttribute("rating", currentUser.getWatched().get(movie.get().getTitle()));
            }
            else{
                model.addAttribute("autoselect", 0);
            }
        }
        return "pages/movieDetails";
    }

    @PostMapping("/movieDetails")
    public ModelAndView updateUser(Authentication auth, Model model, @RequestParam("watched-status") Integer updateUser, @AuthenticationPrincipal OAuth2User user, HttpServletRequest request){
        User userObject;
        if(user == null){
            userObject = ((User)auth.getPrincipal());
            userObject = userRepository.findByTwitterId(userObject.getTwitterId()).get();
        }
        else{
            userObject = userRepository.findByEmail(user.getAttribute("email")).get();
        }

        String title = (String)request.getSession().getAttribute("movie");
        System.out.println(updateUser);
        if(updateUser == 0){
            if(userObject.getWatched() != null && userObject.getWatched().containsKey(title)){
                if(userObject.getGenres() != null){
                    List<String> genres = Arrays.asList(movieRepository.findByTitle(title).get().getGenre().split(", "));
                    for(String genre : genres){
                        userObject.getGenres().put(genre, userObject.getGenres().get(genre)-1);
                    }
                }
                if(userObject.getActors() != null){
                    List<String> actors = Arrays.asList(movieRepository.findByTitle(title).get().getActors().split(", "));
                    for(String actor: actors){
                        userObject.getActors().put(actor, userObject.getActors().get(actor)-1);
                    }
                }
                userObject.getWatched().remove(title);
            }
            /*Iterator<Entry<Movie, Integer>> iterator = userObject.getWatched().entrySet().iterator();
            while(iterator.hasNext()){
                if(iterator.next().getKey().getTitle() == model.getAttribute("title")){
                    iterator.remove();
                    break;
                }
            }*/
        }
        else if (updateUser == 1){
            if(userObject.getWatched() == null){
                System.out.println("uu = 1, wa = null");
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put(title, 5);
                userObject.setWatched(map);
            }
            else{
                userObject.getWatched().put(title, 5);
                
            }
            if(userObject.getActors() == null){
                HashMap<String, Integer> actorsMap= new HashMap<>();
                userObject.setActors(actorsMap);
            }
            if(userObject.getGenres() == null){
                HashMap<String, Integer> genresMap = new HashMap<>();
                userObject.setGenres(genresMap);
            }
            List<String> genres = Arrays.asList(movieRepository.findByTitle(title).get().getGenre().split(", "));
            for(String genre : genres){
                if(userObject.getGenres().containsKey(genre)){
                    userObject.getGenres().put(genre, userObject.getGenres().get(genre)+1);
                }
                else{
                    userObject.getGenres().put(genre, 1);
                }
            }
            List<String> actors = Arrays.asList(movieRepository.findByTitle(title).get().getActors().split(", "));
            for(String actor: actors){
                if(userObject.getActors().containsKey(actor)){
                    userObject.getActors().put(actor, userObject.getActors().get(actor)+1);
                }
                else {
                    userObject.getActors().put(actor, 1);
                }
            }
        }
        userRepository.save(userObject);
        System.out.println("about to redirect");
        return new ModelAndView("redirect:/movieDetails/" + title);
    }

    @PostMapping("/updateRating")
    public ModelAndView updateRating(Authentication auth, Model model, @RequestParam("rating") Integer rating, @AuthenticationPrincipal OAuth2User user, HttpServletRequest request){
        User userObject;
        if(user == null){
            userObject = ((User)auth.getPrincipal());
            userObject = userRepository.findByTwitterId(userObject.getTwitterId()).get();
        }
        else{
            userObject = userRepository.findByEmail(user.getAttribute("email")).get();
        }
        
        String title = (String)request.getSession().getAttribute("movie");
        userObject.getWatched().put(title, rating);
        userRepository.save(userObject);


        return new ModelAndView("redirect:/movieDetails/" + title);
    }
}
