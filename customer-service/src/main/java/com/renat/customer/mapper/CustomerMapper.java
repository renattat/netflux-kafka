package com.renat.customer.mapper;

import com.renat.customer.dto.CustomerDetails;
import com.renat.customer.entity.Customer;
import com.netflux.events.CustomerGenreUpdatedEvent;

import java.time.Instant;

public class CustomerMapper {

    public static CustomerDetails toCustomerDetails(Customer customer){
        return new CustomerDetails(
                customer.getId(),
                customer.getName(),
                customer.getFavoriteGenre()
        );
    }

    public static CustomerGenreUpdatedEvent toGenreUpdatedEvent(Integer customerId, String favoriteGenre) {
        return new CustomerGenreUpdatedEvent(
                customerId,
                favoriteGenre,
                Instant.now()
        );
    }

}
