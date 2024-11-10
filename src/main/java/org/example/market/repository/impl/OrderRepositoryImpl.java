package org.example.market.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.market.domain.Order;
import org.example.market.repository.OrderRepository;
import org.example.market.repository.jpa.OrderJpaRepository;
import org.example.market.status.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public List<Order> findAllByProductId(Long productId) {
        return orderJpaRepository.findAllByProductId(productId);
    }

    @Override
    public List<Order> findAllByBuyerIdAndOrderStatus(Long buyerId, OrderStatus orderStatus) {
        return orderJpaRepository.findAllByBuyerIdAndOrderStatus(buyerId, orderStatus);
    }

    @Override
    public List<Order> findAllBySellerIdAndOrderStatus(Long sellerId, OrderStatus orderStatus) {
        return orderJpaRepository.findAllBySellerIdAndOrderStatus(sellerId, orderStatus);
    }

    @Override
    public List<Order> findAllBySellerIdOrBuyerId(Long sellerId, Long buyerId) {
        return orderJpaRepository.findAllBySellerIdOrBuyerId(sellerId, buyerId);
    }

    @Override
    public boolean existsByBuyerId(Long buyerId) {
        return orderJpaRepository.existsByBuyerId(buyerId);
    }

    @Override
    public void save(Order order) {
        orderJpaRepository.save(order);
    }

}
