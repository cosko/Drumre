package com.labos.lab1.movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {

    public Optional<Movie> findByTitle(String title);
    public Movie findByUniqueId(String uniqueId);
    
}
