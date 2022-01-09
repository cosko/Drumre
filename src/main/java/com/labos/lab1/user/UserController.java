package com.labos.lab1.user;

import java.security.Principal;
import java.util.Map;

import com.labos.lab1.integration.FacebookService;
import com.labos.lab1.integration.LikedPageList;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserController {

  private final UserService userService;

  private final FacebookService fbService;

  public UserController(UserService userService, FacebookService fbService){
    this.userService = userService;
    this.fbService = fbService;
  }

  @GetMapping
  public String getUser(Model model, Authentication auth){
    // LikedPageList userLikedPages = fbService.getUserLikedPages();

    model.addAttribute("user", userService.getUserFromAuth(auth));
    return "pages/profile";
  }
}
