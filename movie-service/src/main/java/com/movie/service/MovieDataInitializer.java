package com.movie.service;

import com.movie.dto.MovieDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service // import the movies for demo purposes
@ConditionalOnProperty(name = "app.import-movies", havingValue = "true")
public class MovieDataInitializer implements CommandLineRunner {

    private final MovieService movieService;
    private final Resource resource;

    public MovieDataInitializer(MovieService movieService, @Value("classpath:movies.jsonl") Resource resource) {
        this.movieService = movieService;
        this.resource = resource;
    }

    @Override
    public void run(String... args) throws Exception {
        Thread.ofVirtual().start(this::loadMovies);
    }

    private void loadMovies() {
        var mapper = JsonMapper.shared();
        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            reader.lines()
                    .map((String line) -> mapper.readValue(line, MovieDetails.class))
                    .forEach((MovieDetails movieDetails) -> {
                        this.movieService.saveMovie(movieDetails);
                        this.sleep(3000);
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
