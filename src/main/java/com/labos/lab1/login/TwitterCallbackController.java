package com.labos.lab1.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Controller
public class TwitterCallbackController {
    
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
            System.out.println("oh no " + token.toString());

            request.getSession().removeAttribute("requestToken");
            request.getSession().setAttribute("username", twitter.getScreenName());
            response.addCookie(new Cookie("username", twitter.getScreenName()));
            response.addCookie(new Cookie("userId", Long.toString(twitter.getId())));

            model.addAttribute("username", twitter.getScreenName());
            
            return "index";
        } catch(Exception e){
            System.out.println("error lol");
            System.out.println(e.getMessage());
            return "redirect:";
        }
    }
}