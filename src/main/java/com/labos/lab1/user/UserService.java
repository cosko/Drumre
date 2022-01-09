package com.labos.lab1.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public Optional<User> getUser(String email) {
    return userRepository.findByEmail(email);
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
