package com.labos.lab1.integration;

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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class FacebookService {
    private String accessToken;
    private WebClient webClient;
    private OAuth2AuthorizedClientService oAuthClientService;
    private static final String GRAPH_API_BASE_URL =
            "https://graph.facebook.com/v12.0";

    public FacebookService(OAuth2AuthorizedClientService clientService) {
        this.oAuthClientService = clientService;
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

    public boolean userAuthorized(){
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
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

    private String getAccessToken() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client =
                oAuthClientService.loadAuthorizedClient(
                        oAuth2Token.getAuthorizedClientRegistrationId(),
                        oAuth2Token.getName());

        return client.getAccessToken().getTokenValue();
    }
}