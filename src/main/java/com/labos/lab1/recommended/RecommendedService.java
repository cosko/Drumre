package com.labos.lab1.recommended;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.labos.lab1.movie.Movie;
import com.labos.lab1.movie.MovieRepository;
import com.labos.lab1.user.User;
import com.labos.lab1.user.UserService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class RecommendedService {

  private final MovieRepository movieRepository;
  private final UserService userService;

  public RecommendedService(MovieRepository movieRepository, UserService userService) {
    this.movieRepository = movieRepository;
    this.userService = userService;
  }

  public List<Movie> findRecommendedMovies(Map<String, Integer> genres, Map<String, Integer> actors, User user) {
    List<String> genresList = genres.keySet().stream().toList();
    List<String> actorsList = actors.keySet().stream().toList();
    Set<Movie> allPotentialMovies = new HashSet<>();
    genresList.forEach(genre -> allPotentialMovies.addAll(movieRepository.findFilteredByGenre(genre)));
    actorsList.forEach(actor -> allPotentialMovies.addAll(movieRepository.findFilteredByActor(actor)));
    return calculateScoreAndSort(allPotentialMovies, genres, actors, user);
  }

  private List<Movie> calculateScoreAndSort(Set<Movie> allPotentialMovies, Map<String, Integer> genres, Map<String, Integer> actors, User user) {
    List<Pair<Movie, Double>> recommendedMovies = new ArrayList<>();

    allPotentialMovies.forEach(movie -> {
      List<String> movieGenres = Arrays.asList(movie.getGenre().split(", "));
      List<String> movieActors = Arrays.asList(movie.getActors().split(", "));
      Double score = (double) 0;
      for(String genre: movieGenres){
        if (genres.containsKey(genre)){
          score += genres.get(genre);
        }
      }
      for(String actor: movieActors){
        if (actors.containsKey(actor)){
          score += actors.get(actor);
        }
      }
      recommendedMovies.add(Pair.of(movie, score));
    });
    recommendedMovies.sort((o1, o2) -> (int) (o2.getSecond() - o1.getSecond()));
    user.setRecommended(recommendedMovies);
    return recommendedMovies.stream().filter(movie -> movie.getSecond() > 0).map(Pair::getFirst).collect(Collectors.toList());
  }
}
