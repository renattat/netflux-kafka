package com.renat.recommendation;

import com.netflux.events.CustomerGenreUpdatedEvent;
import com.netflux.events.MovieAddedEvent;
import com.renat.recommendation.repository.CustomerGenreRepository;
import com.renat.recommendation.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import java.util.List;

@SpringBootTest
@EnableTestBinder
public class RecommendationServiceBinderTest {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private CustomerGenreRepository customerGenreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void customerGenreUpdatedEvent() {
        var event = new CustomerGenreUpdatedEvent(1, "Action", Instant.now());
        this.inputDestination.send(MessageBuilder.withPayload(event).build(), "customer-events");
        var customerGenre = this.customerGenreRepository.findById(1).orElseThrow();
        Assertions.assertEquals("Action", customerGenre.getFavoriteGenre());
    }

    @Test
    public void movieAddedEvent() {
        var event = new MovieAddedEvent(1, "Inception", 100, 110, null, List.of("Action"), null, Instant.now());
        this.inputDestination.send(MessageBuilder.withPayload(event).build(), "movie-events");
        var movie = this.movieRepository.findById(1).orElseThrow();
        Assertions.assertEquals("Inception", movie.getTitle());
    }

}
