package com.aroom.domain.cart.controller;

import com.aroom.domain.cart.dto.response.CartResponse;
import com.aroom.domain.cart.service.CartService;
import com.aroom.global.resolver.Login;
import com.aroom.global.resolver.LoginInfo;
import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
public class CartRestController {

    private final CartService cartService;

    @PostMapping("/{member_id}")
    public ResponseEntity<ApiResponse<CartResponse>> postCart(@PathVariable long member_id) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse(LocalDateTime.now(), "성공적으로 장바구니를 등록했습니다.",
                cartService.postCart(member_id)));
    }
}
