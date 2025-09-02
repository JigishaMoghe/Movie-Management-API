package com.example.movieapi.repository;

import com.example.movieapi.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MovieRepository {

    private final Map<String, Movie> store = new LinkedHashMap<>();


    public synchronized List<Movie> findAll() {
        return new ArrayList<>(store.values());
    }

 public synchronized Optional<Movie> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }


    public synchronized Movie save(Movie movie) {
        store.put(movie.getId(), movie);
        return movie;
    }

    public synchronized void deleteById(String id) {
        store.remove(id);
    }

    public synchronized boolean existsById(String id) {
        return store.containsKey(id);
    }


    public synchronized void clear() {
        store.clear();
    }
}