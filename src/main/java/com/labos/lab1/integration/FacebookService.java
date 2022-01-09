package com.labos.lab1.integration;

import com.labos.lab1.movie.Movie;
import com.labos.lab1.movie.MovieService;
import com.labos.lab1.user.User;
import com.labos.lab1.user.UserService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class FacebookService {
    private static final Integer USER_LIKED_MOVIE_RATE_TO_MAPPING = 8;
    private static final Integer USER_LIKED_ACTOR_RATE_TO_MAPPING = 7;
    private final MovieService movieService;
    private String accessToken;
    private WebClient webClient;
    private OAuth2AuthorizedClientService oAuthClientService;
    private static final String GRAPH_API_BASE_URL =
            "https://graph.facebook.com/v12.0";

    public FacebookService(OAuth2AuthorizedClientService clientService,
                           MovieService movieService) {
        this.oAuthClientService = clientService;
        this.movieService = movieService;
        this.webClient = createClient();
    }

    public LikedPageList getUserLikedPages() {
        if(!userAuthorized()){
            return new LikedPageList();
        }
        Mono<LikedPageList> result = webClient.get()
                .uri("/me/likes")
                .header("Authorization", "Bearer " + getAccessToken())
                .retrieve()
                .bodyToMono(LikedPageList.class);
        return result.block();
    }

    public User setUserLikedMovies(User user){
        LikedPageList userLikedPages = getUserLikedPages();
        List<String> likedPageNames = userLikedPages.getLikedPages().stream().map((LikedPage::getName)).toList();

        List<Movie> movies = movieService.findMultiple(likedPageNames);
        List<Movie> withActors = movieService.findAllWithAnyOfActors(likedPageNames);

        //User currUser = userService.getUserFromAuth(getAuth());

        for(Movie movie : withActors){
            if(!user.getWatched().containsKey(movie.getUniqueId()))
                user.setWatchedEntry(movie.getUniqueId(), USER_LIKED_ACTOR_RATE_TO_MAPPING);
        }

        for(Movie movie : movies){
            if(!user.getWatched().containsKey(movie.getUniqueId()))
                user.setWatchedEntry(movie.getUniqueId(), USER_LIKED_MOVIE_RATE_TO_MAPPING);
        }

        //userService.saveUser(currUser);
        return user;
    }

    public boolean userAuthorized(){
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) getAuth();
        return Objects.equals(oAuth2Token.getAuthorizedClientRegistrationId(), "facebook");
    }

    private WebClient createClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(GRAPH_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    private Authentication getAuth() {
        return SecurityContextHolder
                        .getContext()
                        .getAuthentication();
    }

    private String getAccessToken() {
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) getAuth();
        OAuth2AuthorizedClient client =
                oAuthClientService.loadAuthorizedClient(
                        oAuth2Token.getAuthorizedClientRegistrationId(),
                        oAuth2Token.getName());

        return client.getAccessToken().getTokenValue();
    }
}