package com.labos.lab1.user;

import java.util.List;
import java.util.Optional;

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
}
