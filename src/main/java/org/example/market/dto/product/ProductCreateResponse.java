package org.example.market.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.market.dto.user.UserResponse;

@Getter
@Setter
@Builder
public class ProductCreateResponse {

    private ProductResponse product;
    private UserResponse seller;

}
