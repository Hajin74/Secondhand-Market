package org.example.market.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.market.domain.Order;
import org.example.market.status.OrderStatus;

@Getter
@Setter
@Builder
public class OrderResponse {

    private Long id;
    private Integer quantity;
    private Integer orderedPrice;
    private OrderStatus orderStatus;
    private Long productId;
    private Long buyerId;

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .quantity(order.getQuantity())
                .orderedPrice(order.getOrderedPrice())
                .orderStatus(order.getOrderStatus())
                .productId(order.getProductId())
                .buyerId(order.getBuyerId())
                .build();
    }

}
