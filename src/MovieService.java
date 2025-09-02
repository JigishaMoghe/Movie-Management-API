package com.example.movieapi.service;

import com.example.movieapi.dto.MovieCreateDto;
import com.example.movieapi.dto.MovieUpdateDto;
import com.example.movieapi.model.Movie;
import com.example.movieapi.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class MovieService {
    private final MovieRepository repo;

    public MovieService(MovieRepository repo) {
        this.repo = repo;
    }


    public List<Movie> list() {

        return repo.findAll();
    }

 
    public Movie get(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

  
    public Movie create(MovieCreateDto dto) {
        String id = UUID.randomUUID().toString();
        Movie m = new Movie(id, dto.getTitle(), dto.getDirector(), dto.getReleaseYear(), dto.getGenre(), dto.getRating());
        return repo.save(m);
    }


    public Movie update(String id, MovieUpdateDto dto) {
        Movie existing = get(id);
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDirector() != null) existing.setDirector(dto.getDirector());
        if (dto.getReleaseYear() != null) existing.setReleaseYear(dto.getReleaseYear());
        if (dto.getGenre() != null) existing.setGenre(dto.getGenre());
        if (dto.getRating() != null) existing.setRating(dto.getRating());
        return repo.save(existing);
    }

 
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        repo.deleteById(id);
    }

  
    public void clear() {
        repo.clear();
    }
}