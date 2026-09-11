package com.renat.recommendation.messaging;

import com.netflux.events.CustomerGenreUpdatedEvent;
import com.netflux.events.MovieAddedEvent;
import com.renat.recommendation.service.CustomerService;
import com.renat.recommendation.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    private static final Logger log = LoggerFactory.getLogger(EventConsumerConfig.class);

    @Bean
    public Consumer<CustomerGenreUpdatedEvent> genreUpdatedEventConsumer(CustomerService customerService) {
        return withLogging(customerService::updateGenre);
    }


    @Bean
    public Consumer<MovieAddedEvent> movieAddedEventConsumer(MovieService movieService){
        return withLogging(movieService::addMovie);
    }

    private <T> Consumer<T> withLogging(Consumer<T> consumer) {
         return t -> {
             log.info("received: {}", t);
             consumer.accept(t);
         };
    }


}
