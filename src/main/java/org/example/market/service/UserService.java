package org.example.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.market.dto.user.UserJoinRequest;
import org.example.market.dto.user.UserResponse;
import org.example.market.exception.CustomException;
import org.example.market.exception.ErrorCode;
import org.example.market.domain.User;
import org.example.market.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


    @Transactional
    public UserResponse joinUser(UserJoinRequest joinDto) {
        String username = joinDto.getUsername();
        String password = joinDto.getPassword();

        validateUsername(username);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        newUser.setRole("ROLE_USER");

        userRepository.save(newUser);

        return UserResponse.from(newUser);
    }

    private void validateUsername(String username) {
        User findUser = userRepository.findByUsername(username);
        if (findUser != null) {
            throw new CustomException(ErrorCode.USERNAME_ALREADY_IN_USE);
        }
    }

}
