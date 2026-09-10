package com.movie;

import com.movie.dto.MovieDetails;
import com.netflux.events.MovieAddedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;


@EnableTestBinder
@AutoConfigureRestTestClient
@SpringBootTest(properties = "app.import-movies=false")
public class MovieEventBinderTest {

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void movieAddedEvent() {
        // send the post request
        var json = """
                {"title":"The Fifth Element","voteAverage":7.53,"voteCount":9947,"releaseDate":"1997-05-02","revenue":263920180,"runtime":126,"backdropPath":"/wgvc3PmjQGtYYDWaeuV867mnFDs.jpg","budget":90000000,"homepage":"","overview":"In 2257, a taxi driver is unintentionally given the task of saving a young girl who is part of the key that will ensure the survival of humanity.","popularity":49.67,"posterPath":"/fPtlCO1yQtnoLHOwKtWz7db6RGU.jpg","genres":["Adventure","Fantasy","Action","Thriller","Science Fiction"]}
                """;
        var request = JsonMapper.shared().readValue(json, MovieDetails.class);
        var response = this.testClient.post()
                .uri("/api/movies")
                .body(request)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(MovieDetails.class)
                .getResponseBody();

        // validate the movie response
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals("The Fifth Element", response.title());

        // validate movie added event
        var message = this.outputDestination.receive(1000, "movie-events");
        var event = JsonMapper.shared().readValue(message.getPayload(), MovieAddedEvent.class);
        Assertions.assertEquals(response.id(), message.getHeaders().get(KafkaHeaders.KEY, Integer.class));
        Assertions.assertEquals(response.id(), event.movieId());
        Assertions.assertEquals("The Fifth Element", event.title());
    }

}
