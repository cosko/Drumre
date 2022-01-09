package com.labos.lab1.movie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.labos.lab1.user.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private final MovieRepository movieRepository;
  private final MongoTemplate mongoTemplate;

  public MovieService(MovieRepository movieRepository,
                      MongoTemplate mongoTemplate) {
    this.movieRepository = movieRepository;
    this.mongoTemplate = mongoTemplate;
  }

  public void save(String data) throws JsonProcessingException {
    Gson gson = new Gson();
    Movie movie = gson.fromJson(data, Movie.class);
    movieRepository.save(movie);
  }

  public void saveMovie(Movie movie){
    movieRepository.save(movie);
  }

  public List<Movie> getAll(){
    return movieRepository.findAll();
  }

  public Movie getByUniqueId(String uniqueId){ return movieRepository.findByUniqueId(uniqueId); }

  public long collectionSize(){
    return movieRepository.count();
  }

  public List<Movie> findBestMovies() {
    return movieRepository.findBestMovies();
  }

  public List<Movie> findFilteredMovies(MovieFilter filter) {
    return movieRepository.findFiltered(filter.getTitle(), filter.getGenre(), filter.getActors());
  }

  public List<Movie> findMultiple(List<String> titles) {
      String[] titleArr = titles.toArray(new String[0]);
      return movieRepository.findAllBy(titles.toArray(new String[0]));
  }
  public List<Movie> findAllWithAnyOfActors(List<String> actors) {
    List<Movie> results = new ArrayList<Movie>();
    for(String actor : actors) {
        results.addAll(movieRepository.findFilteredByActor(actor));
    }
    return results;
  }

  public List<Movie> findAllWithActor(String actor) {
    return movieRepository.findFilteredByActor(actor);
  }
}
