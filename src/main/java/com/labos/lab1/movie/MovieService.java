package com.labos.lab1.movie;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository){
    this.movieRepository = movieRepository;
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
}
