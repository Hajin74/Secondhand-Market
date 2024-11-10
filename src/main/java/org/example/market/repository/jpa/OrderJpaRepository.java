package org.example.market.repository.jpa;

import org.example.market.domain.Order;
import org.example.market.status.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByProductId(Long productId);
    List<Order> findAllBySellerIdOrBuyerId(Long sellerId, Long buyerId);
    List<Order> findAllByBuyerIdAndOrderStatus(Long buyerId, OrderStatus orderStatus);
    List<Order> findAllBySellerIdAndOrderStatus(Long sellerId, OrderStatus orderStatus);
    Boolean existsByBuyerId(Long buyerId);

}
