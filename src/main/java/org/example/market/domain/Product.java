package org.example.market.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.market.status.ProductStatus;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private Integer stock;

    private Long ownerId;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Builder
    public Product(String name, Integer price, Integer stock, ProductStatus productStatus, Long ownerId) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.productStatus = productStatus;
        this.ownerId = ownerId;
    }

    public void modifyProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public void decreaseStock(Integer quantity) {
        this.stock -= quantity;
    }

    public void modifyPrice(Integer price) {
        this.price = price;
    }

    public boolean isProductOwner(Long userId) {
        return this.ownerId.equals(userId);
    }

    public boolean isSameFromPreviousPrice(Integer updatedPrice) {
        return this.price.equals(updatedPrice);
    }

    public boolean isEnoughStockForOrder(Integer quantity) {
        return this.getStock() >= quantity;
    }

    public boolean isEnoughStockForNextOrder() {
        return this.getStock() > 0;
    }

}
