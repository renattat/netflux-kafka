package com.renat.recommendation.service;

import com.netflux.events.MovieAddedEvent;
import com.renat.recommendation.mapper.RecommendationMapper;
import com.renat.recommendation.repository.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    public void addMovie(MovieAddedEvent movieAddedEvent){
        var entity = RecommendationMapper.toMovie(movieAddedEvent);
        this.movieRepository.save(entity);
    }

}
