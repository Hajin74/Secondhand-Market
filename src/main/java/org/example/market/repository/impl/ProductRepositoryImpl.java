package org.example.market.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.market.domain.Product;
import org.example.market.repository.ProductRepository;
import org.example.market.repository.jpa.ProductJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findByIdWithPessimisticLock(id);
    }

    @Override
    public List<Product> findAllByOwnerId(Long ownerId) {
        return productJpaRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public void save(Product product) {
        productJpaRepository.save(product);
    }

}
