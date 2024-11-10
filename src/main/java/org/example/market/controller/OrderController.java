package org.example.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.market.dto.common.DataResponse;
import org.example.market.dto.common.ErrorResponse;
import org.example.market.dto.common.ApiResponse;
import org.example.market.dto.order.OrderCreateRequest;
import org.example.market.dto.user.CustomUserDetails;
import org.example.market.exception.CustomException;
import org.example.market.repository.jpa.UserJpaRepository;
import org.example.market.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserJpaRepository userRepository;

    /* 제품 주문 */

    @PostMapping
    public ApiResponse orderProduct(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid OrderCreateRequest request) {
        log.info("제품 주문 api");

        String username = customUserDetails.getUsername();
        Long userId = userRepository.findByUsername(username).getId();

        try {
            return new DataResponse<>(orderService.orderProduct(userId, request));
        } catch (CustomException exception) {
            return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        }
    }

    /* 내 거래내역 보기 */
    @GetMapping("/me")
    public ApiResponse getMyTransactionList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("내 거래내역 보기 api");

        String username = customUserDetails.getUsername();
        Long userId = userRepository.findByUsername(username).getId();

        try {
            return new DataResponse<>(orderService.findMyTransactionList(userId));
        } catch (CustomException exception) {
            return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        }
    }

    /* 판매 승인 */
    @PatchMapping("/{orderId}/approve")
    public ApiResponse approveProductOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        log.info("판매 승인 api");

        String username = customUserDetails.getUsername();
        Long userId = userRepository.findByUsername(username).getId();

        try {
            return new DataResponse<>(orderService.approveProductOrder(userId, orderId));
        } catch (CustomException exception) {
            return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        }
    }

    /* 구매 확정 */
    @PatchMapping("/{orderId}/confirm")
    public ApiResponse confirmProductOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long orderId) {
        log.info("구매 확정 api");

        String username = customUserDetails.getUsername();
        Long userId = userRepository.findByUsername(username).getId();

        try {
            return new DataResponse<>(orderService.confirmProductOrder(userId, orderId));
        } catch (CustomException exception) {
            return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
        }
    }

}
