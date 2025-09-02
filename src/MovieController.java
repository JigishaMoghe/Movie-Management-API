package com.example.movieapi.controller;

import com.example.movieapi.dto.MovieCreateDto;
import com.example.movieapi.dto.MovieUpdateDto;
import com.example.movieapi.model.Movie;
import com.example.movieapi.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public List<Movie> list(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "50") int size) {
        List<Movie> all = service.list();
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        return all.subList(from, to);
    }

    @GetMapping("/{id}")
    public Movie get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie create(@Valid @RequestBody MovieCreateDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable String id, @Valid @RequestBody MovieUpdateDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/test/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearForTests() {
        service.clear();
    }
}