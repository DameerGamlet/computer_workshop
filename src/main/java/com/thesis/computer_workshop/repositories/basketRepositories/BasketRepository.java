package com.thesis.computer_workshop.repositories.basketRepositories;

import com.thesis.computer_workshop.models.basket.Basket;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, Long> {
}
