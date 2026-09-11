package com.renat.recommendation.service;

import com.netflux.events.MovieAddedEvent;
import com.renat.recommendation.dto.RecommendationEvents;
import com.renat.recommendation.mapper.RecommendationMapper;
import com.renat.recommendation.repository.MovieRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MovieService(MovieRepository movieRepository, ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.movieRepository = movieRepository;
    }


    public void addMovie(MovieAddedEvent movieAddedEvent){
        var entity = RecommendationMapper.toMovie(movieAddedEvent);
        this.movieRepository.save(entity);
        this.eventPublisher.publishEvent(new RecommendationEvents.NewMovieEvent(movieAddedEvent.movieId()));
    }

}
