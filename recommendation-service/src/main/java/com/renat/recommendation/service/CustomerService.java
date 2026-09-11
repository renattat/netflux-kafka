package com.renat.recommendation.service;

import com.netflux.events.CustomerGenreUpdatedEvent;
import com.renat.recommendation.mapper.RecommendationMapper;
import com.renat.recommendation.repository.CustomerGenreRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerGenreRepository customerGenreRepository;

    public CustomerService(CustomerGenreRepository customerGenreRepository) {
        this.customerGenreRepository = customerGenreRepository;
    }

    public void updateGenre(CustomerGenreUpdatedEvent genreUpdatedEvent){
        var entity = RecommendationMapper.toCustomerGenre(genreUpdatedEvent);
        this.customerGenreRepository.save(entity);

    }

}
