package com.labos.lab1.login;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Controller
public class GetTwitterTokenController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetTwitterTokenController.class);
	@Autowired
    private Environment env;

	@GetMapping("/getToken")
	public RedirectView getToken(HttpServletRequest request, Model model) {
		String twitterUrl = "http://localhost:8080/";

		try {
			Twitter twitter = getTwitter();
			String callbackUrl = "http://localhost:8080/twitterCallback";
			RequestToken requestToken = twitter.getOAuthRequestToken(callbackUrl);

			request.getSession().setAttribute("requestToken", requestToken);
			request.getSession().setAttribute("twitter", twitter);

			twitterUrl = requestToken.getAuthorizationURL();

			LOGGER.info("Authorization url is " + twitterUrl);
		} catch (Exception e) {
			LOGGER.error("Problem logging in with Twitter!", e);
		}

		//redirect to the Twitter URL
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(twitterUrl);
		return redirectView;
	}
    
	public Twitter getTwitter() {
		Twitter twitter  = null;

        String consumerKey = env.getProperty("twitter.api.key");
		String consumerSecret = env.getProperty("twitter.api.secret");
		String accessToken = env.getProperty("twitter.access.token");
		String accessSecret = env.getProperty("twitter.access.secret");
        
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(consumerKey);
		builder.setOAuthConsumerSecret(consumerSecret);
		Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		return twitter;
	}
}