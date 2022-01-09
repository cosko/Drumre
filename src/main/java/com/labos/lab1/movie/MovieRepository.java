package com.labos.lab1.movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MovieRepository extends MongoRepository<Movie, String> {

    Optional<Movie> findByTitle(String title);
    Movie findByUniqueId(String uniqueId);

    @Query(value = "{'imdbRating' : {$gt: '8.0', $not: {$eq: 'N/A'}}}", sort = "{'imdbRating': -1}")
    List<Movie> findBestMovies();
    
    @Query(value = "{'imdbRating' : {$gt: '8.0', $not: {$eq: 'N/A'}}, 'year': {$in: ['2021', '2020']}}", sort = "{'imdbRating': -1}")
    List<Movie> findPopularMovies();

    @Query(value= "{'title': {$regex : ?0, $options: 'i'}," +
        "           'genre': {$regex : ?1, $options: 'i'}," +
        "           'actors': {$regex : ?2, $options: 'i'}}")
    List<Movie> findFiltered(String title, String genre, String actor);
}
