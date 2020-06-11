package com.github.fabriciolfj.demowebfluxmongo.domain.repositories;

import com.github.fabriciolfj.demowebfluxmongo.domain.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
