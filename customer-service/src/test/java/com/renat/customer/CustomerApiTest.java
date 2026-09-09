package com.renat.customer;

import com.renat.customer.dto.CustomerDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
public class CustomerApiTest {

    @Autowired
    private RestTestClient testClient;

    @Test
    public void customerDetails() {
        var dto = this.testClient.get()
                .uri("/api/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
//                .expectBody()
//                .jsonPath("$.id").isEqualTo(1)
//                .jsonPath("$.name").isEqualTo("Sam")
//                .jsonPath("$.favoriteGenre").isEqualTo("Action");
                .returnResult(CustomerDetails.class)
                .getResponseBody();

        Assertions.assertEquals(1, dto.id());
        Assertions.assertEquals("Sam", dto.name());
        Assertions.assertEquals("Action", dto.favoriteGenre());


    }

    @Test
    public void customerNotFound() {
        this.testClient.get()
                .uri("/api/customers/100")
                .exchange()
                .expectStatus().is4xxClientError();
    }

}
