package com.renat.customer.service;

import com.renat.customer.dto.CustomerDetails;
import com.renat.customer.dto.GenreUpdateRequest;
import com.renat.customer.exception.CustomerNotFoundException;
import com.renat.customer.mapper.CustomerMapper;
import com.renat.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerService(CustomerRepository customerRepository, ApplicationEventPublisher eventPublisher) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    public CustomerDetails getCustomer(Integer customerId) {
        return this.customerRepository.findById(customerId)
                .map(CustomerMapper::toCustomerDetails)
                .orElseThrow(()-> new CustomerNotFoundException(customerId));
    }

    @Transactional
    public void updateCustomerGenre(Integer customerId, GenreUpdateRequest request) {
        var customer = this.customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException(customerId));
        customer.setFavoriteGenre(request.favoriteGenre());
        this.eventPublisher.publishEvent(CustomerMapper.toGenreUpdatedEvent(customer.getId(), request.favoriteGenre()));
    }

}
