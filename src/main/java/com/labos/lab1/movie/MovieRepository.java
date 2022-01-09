package com.labos.lab1.movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MovieRepository extends MongoRepository<Movie, String> {

    public Optional<Movie> findByTitle(String title);
    public Movie findByUniqueId(String uniqueId);

    @Query(value= "{'title': {$regex : ?0, $options: 'i'}}")
    List<Movie> findByTitleRegex(String regexString);

    @Query(value = "{'imdbRating' : {$gt: '8.0', $not: {$eq: 'N/A'}}}", sort = "{'imdbRating': -1}")
    List<Movie> findBestMovies();
    
    @Query(value = "{'imdbRating' : {$gt: '8.0', $not: {$eq: 'N/A'}}, 'year': {$in: ['2021', '2020']}}", sort = "{'imdbRating': -1}")
    List<Movie> findPopularMovies();
}
