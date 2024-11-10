package org.example.market.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.market.dto.order.TransactionResponse;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductDetailResponse {

    private ProductResponse product;
    private List<TransactionResponse> transactions;
}
