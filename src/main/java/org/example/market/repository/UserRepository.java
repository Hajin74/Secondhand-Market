package org.example.market.repository;

import org.example.market.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long userId);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    void save(User user);

}
