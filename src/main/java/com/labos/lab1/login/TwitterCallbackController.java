package com.labos.lab1.login;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labos.lab1.user.User;
import com.labos.lab1.user.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Controller
public class TwitterCallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterCallbackController.class);

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/twitterCallback")
    public String twitterCallback(@RequestParam(value="oauth_verifier", required=false) String oauthVerifier,
                                  @RequestParam(value="denied", required=false) String denied,
                                  HttpServletRequest request, HttpServletResponse response, Model model) {

        if (denied != null) {
            return "redirect:login";
        }

        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");

        try {
            AccessToken token = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            User newUser;
            if(userRepository.findByTwitterId(twitter.getId()).isEmpty()){
                newUser = new User(twitter.getScreenName(), twitter.getId(), twitter.showUser(twitter.getId()).get400x400ProfileImageURL());
                userRepository.save(newUser);
            }
            else{
                newUser = userRepository.findByTwitterId(twitter.getId()).get();
            }
            Authentication auth = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            return "redirect:/";
        } catch (Exception e) {
            LOGGER.error("Problem getting token!",e);
            return "redirect:login";
        }
    }
}
