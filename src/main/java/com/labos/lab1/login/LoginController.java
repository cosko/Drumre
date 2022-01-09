package com.labos.lab1.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.labos.lab1.integration.FacebookService;
import com.labos.lab1.user.User;
import com.labos.lab1.user.UserService;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

@Controller
public class LoginController {

  private static String authorizationRequestBaseUri = "oauth2/authorization";
  Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

  private ClientRegistrationRepository clientRegistrationRepository;
  private OAuth2AuthorizedClientService authorizedClientService;
  private UserService userService;
  private FacebookService facebookService;

  public LoginController(ClientRegistrationRepository clientRegistrationRepository,
                         OAuth2AuthorizedClientService authorizedClientService,
                         UserService userService,
                         FacebookService facebookService){
    this.clientRegistrationRepository = clientRegistrationRepository;
    this.authorizedClientService = authorizedClientService;
    this.userService = userService;
    this.facebookService = facebookService;
  }

  @GetMapping("/login")
  public String getLoginView(Model model,
                             @RequestParam(value = "logout", required = false) String logout) {
    Iterable<ClientRegistration> clientRegistrations = null;
    ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                                        .as(Iterable.class);
    if (type != ResolvableType.NONE &&
        ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
      clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
    }

    clientRegistrations.forEach(registration ->
                                    oauth2AuthenticationUrls.put(registration.getClientName(),
                                                                 authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
    oauth2AuthenticationUrls.put("Twitter", "/getToken");
    model.addAttribute("urls", oauth2AuthenticationUrls);
    if (logout != null){
      model.addAttribute("message", "Uspješno ste se izlogirali");
    }
    return "login";
  }

  @GetMapping("/loginSuccess")
  public ModelAndView getLogininfo(Model model, OAuth2AuthenticationToken authentication) {
    OAuth2AuthorizedClient client = authorizedClientService
        .loadAuthorizedClient(
            authentication.getAuthorizedClientRegistrationId(),
            authentication.getName());
    String userInfoEndpointUri = client.getClientRegistration()
                                       .getProviderDetails().getUserInfoEndpoint().getUri();

    if (!StringUtils.isEmpty(userInfoEndpointUri)) {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                                                               .getTokenValue());
      HttpEntity<String> entity = new HttpEntity<>("", headers);
      ResponseEntity<Map>response = restTemplate
          .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
      Map userAttributes = response.getBody();

      User user = mapBaseUser(userAttributes);
      if(Objects.equals(authentication.getAuthorizedClientRegistrationId(), "facebook"))
        setFacebookAttrs(user, userAttributes);

      saveUser(user);
    }
    return new ModelAndView("redirect:/");

  }

  private User mapBaseUser(Map<String, Serializable> data) {
      return new User((String) data.get("given_name"),
          (String) data.get("family_name"),
          (String) data.get("picture"),
          (String) data.get("email"),
          (Boolean) data.get("email_verified"));
  }

    private void setFacebookAttrs(User user, Map<String, Serializable> data) {
      user.setFacebookId(Long.valueOf((String) data.get("id")));
      String fullName = (String) data.get("name");
      String[] nameArr = fullName.split(" ");
      user.setFirstName(nameArr[0]);
      user.setLastName(nameArr[1]);
      saveUser(facebookService.setUserLikedMovies(user));
      //saveUser(user);
    }

    private void saveUser(User user) {
      if (userService.exists(user.getEmail()).isEmpty()){
        userService.saveUser(user);
    }
  }
}
