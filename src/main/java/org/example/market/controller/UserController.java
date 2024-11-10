package org.example.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.market.dto.common.ApiResponse;
import org.example.market.dto.common.DataResponse;
import org.example.market.dto.common.ErrorResponse;
import org.example.market.dto.user.UserJoinRequest;
import org.example.market.exception.CustomException;
import org.example.market.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ApiResponse joinUser(@RequestBody @Valid UserJoinRequest request) {
        // todo : valid exception 처리
        try {
            return new DataResponse<>(userService.joinUser(request));
        } catch (CustomException exception) {
            return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        }
    }

}
