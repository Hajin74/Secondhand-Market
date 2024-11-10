package org.example.market.repository;

import org.example.market.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    List<Product> findAllByOwnerId(Long ownerId);

    List<Product> findAll();

    void save(Product product);

}
