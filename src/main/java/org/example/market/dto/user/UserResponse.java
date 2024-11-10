package org.example.market.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.market.domain.User;

@Getter
@Setter
@Builder
@ToString
public class UserResponse {

    private Long id;
    private String username;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();

    }

}
