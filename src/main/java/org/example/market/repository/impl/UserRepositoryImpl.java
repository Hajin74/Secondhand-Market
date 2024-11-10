package org.example.market.repository.impl;

import lombok.RequiredArgsConstructor;
import org.example.market.domain.User;
import org.example.market.repository.UserRepository;
import org.example.market.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(user);
    }

}
