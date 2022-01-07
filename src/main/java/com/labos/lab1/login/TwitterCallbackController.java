package com.labos.lab1.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labos.lab1.user.User;
import com.labos.lab1.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Controller
public class TwitterCallbackController {

    @Autowired
    private UserRepository userRepository;
    
    @RequestMapping("/twitterCallback")
    public String twitterCallback(@RequestParam(value="oauth_verifier", required=false) String oauthVerifier,
    @RequestParam(value="denied", required=false) String denied,
    HttpServletRequest request, HttpServletResponse response, Model model){
        if (denied != null) {
            return "redirect:index";
        }

        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");

        try {
            AccessToken token = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
            System.out.println("before if");
            if(userRepository.findByTwitterId(twitter.getId()).isEmpty()){
                userRepository.save(new User(twitter.getScreenName(), twitter.getId(), twitter.showUser(twitter.getId()).get400x400ProfileImageURL()));
                System.out.println("spremijo u bazu");
            }
            System.out.println("after if");
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken());
            System.out.println(twitter.getId() + " " + twitter.getScreenName());
            request.getSession().setAttribute("screenname", twitter.getScreenName());
            response.addCookie(new Cookie("username", twitter.getScreenName()));
            response.addCookie(new Cookie("userId", Long.toString(twitter.getId())));

            model.addAttribute("username", twitter.getScreenName());
            
            return "index";
        } catch(Exception e){
            System.out.println(e.getMessage());
            return "redirect:";
        }
    }
}
