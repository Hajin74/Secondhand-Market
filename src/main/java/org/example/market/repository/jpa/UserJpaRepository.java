package org.example.market.repository.jpa;

import org.example.market.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

}
