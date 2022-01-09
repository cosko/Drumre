package com.labos.lab1.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public Optional<User> getUser(String email) {
    return userRepository.findByEmail(email);
  }

  public User getUserFromAuth(Authentication auth) {
      if(auth.getPrincipal() instanceof User){
          return (User) auth.getPrincipal();
      }
      DefaultOAuth2User oauth2user = (DefaultOAuth2User) auth.getPrincipal();
      Optional<User> optUser = userRepository.findByEmail(oauth2user.getAttribute("email"));
      if(optUser.isEmpty())
          throw new RuntimeException("Error fetching user");
      return optUser.get();
  }

  public void saveUser(User user) {
    userRepository.save(user);
  }

  public Optional<User> exists(String email){
    return userRepository.findByEmail(email);
  }

  public User getUniqueUser(Authentication auth, OAuth2User user){
    User currentUser;
        if(user == null){
            currentUser = ((User)auth.getPrincipal());
            currentUser = userRepository.findByTwitterId(currentUser.getTwitterId()).get();
        }
        else{
            currentUser = userRepository.findByEmail(user.getAttribute("email")).get();
        }
    return currentUser;
  }
}
