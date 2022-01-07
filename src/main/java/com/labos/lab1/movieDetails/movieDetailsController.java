package com.labos.lab1.movieDetails;

import java.util.Iterator;
import java.util.Optional;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.labos.lab1.movie.Movie;
import com.labos.lab1.movie.MovieRepository;
import com.labos.lab1.user.User;
import com.labos.lab1.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/movieDetails")
public class movieDetailsController {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;
    
    @GetMapping
    public ModelAndView movieDetails(Model model, @PathVariable("title") String title, @AuthenticationPrincipal OAuth2User user, HttpServletRequest request){
        if(title == "" || title == null){
            return new ModelAndView("redirect:");
        }
        
        Optional<Movie> movie = movieRepository.findByTitle(title);
        if(movie.isEmpty()){
            model.addAttribute("movie", null);
        }
        else{
            request.getSession().setAttribute("movie", movie.get());
            if(userRepository.findByEmail(user.getAttribute("email")).get().getWatched().containsKey(movie.get())){
                model.addAttribute("autoselect", 1);
            }
            else{
                model.addAttribute("autoselect", 0);
            }
            model.addAttribute("movie", movie.get());
        }
        return new ModelAndView("movieDetails");
    }

    @PostMapping
    public ModelAndView updateUser(Model model, @RequestBody Integer updateUser, @AuthenticationPrincipal OAuth2User user, HttpServletRequest request){
        User userObject = userRepository.findByEmail(user.getAttribute("email")).get();
        if(updateUser == 0){
            Iterator<Entry<Movie, Integer>> iterator = userObject.getWatched().entrySet().iterator();
            while(iterator.hasNext()){
                if(iterator.next().getKey().getTitle() == model.getAttribute("title")){
                    iterator.remove();
                    break;
                }
            }
        }
        else if (updateUser == 1){
            userObject.getWatched().put((Movie)request.getAttribute("movie"), 0);
        }
        
        return new ModelAndView("movieDetails");
    }
}
