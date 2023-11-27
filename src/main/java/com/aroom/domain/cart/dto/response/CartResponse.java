package com.aroom.domain.cart.dto.response;

import com.aroom.domain.cart.model.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartResponse {

    private long cart_id;
    private long member_id;

    @Builder
    public CartResponse(long cart_id, long member_id) {
        this.cart_id = cart_id;
        this.member_id = member_id;
    }

    public CartResponse(Cart cart) {
        this.cart_id = cart.getId();
        this.member_id = cart.getMember().getId();
    }
}
