package com.labos.lab1.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.web.servlet.ModelAndView;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Controller
public class GetTwitterTokenController {

    @Autowired
    private Environment env;
		
    @CrossOrigin("https://api.twitter.com")
    @RequestMapping("/getTwitterToken")
    public ModelAndView getTwitterToken(HttpServletRequest request, Model model) {
    	//this will be the URL that we take the user to
    	String twitterUrl = "";
        System.out.println("entered");
    	
		try {
			//get the Twitter object
            System.out.println("getting twitter obj");
			Twitter twitter = getTwitter();
			
			//get the callback url so they get back here
            System.out.println("setting callback url");
			String callbackUrl = "http://localhost:8080/twitterCallback";

			//go get the request token from Twitter
            System.out.println("getting auth request token");
			RequestToken requestToken = twitter.getOAuthRequestToken(callbackUrl);
			
			//put the token in the session because we'll need it later
            System.out.println("setting attributes for session");
			request.getSession().setAttribute("requestToken", requestToken);
			
			//let's put Twitter in the session as well
			request.getSession().setAttribute("twitter", twitter);
			
			//now get the authorization URL from the token
            System.out.println("getting authorization url");
			twitterUrl = requestToken.getAuthorizationURL();
			
		} catch (Exception e) {
            System.out.println("opps, exception");
            System.out.println(e.getMessage());
		}
    	
		//redirect to the Twitter URL
        System.out.println("redirected lol");
        return new ModelAndView("redirect:" + twitterUrl);
    }
    
	public Twitter getTwitter() {
		Twitter twitter  = null;

        System.out.println("setting consumer key and secret");
        String consumerKey = env.getProperty("twitterAPIKey");
		String consumerSecret=env.getProperty("twitterAPIKeySecret");
        //String oauthToken=env.getProperty("twitteroauthToken");
        //String oauthTokenKey=env.getProperty("twitteroauthTokenKey");
        
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(consumerKey);
		builder.setOAuthConsumerSecret(consumerSecret);
        Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();
        System.out.println(twitter.toString());

		return twitter;
	}
}