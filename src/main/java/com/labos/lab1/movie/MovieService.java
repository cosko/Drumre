package com.labos.lab1.movie;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
}
