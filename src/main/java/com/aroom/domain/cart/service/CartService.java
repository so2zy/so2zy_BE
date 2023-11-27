package com.aroom.domain.cart.service;

import com.aroom.domain.cart.dto.response.CartResponse;
import com.aroom.domain.cart.model.Cart;
import com.aroom.domain.cart.repository.CartRepository;
import com.aroom.domain.member.exception.MemberNotFoundException;
import com.aroom.domain.member.model.Member;
import com.aroom.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    public CartResponse postCart(long member_id){
        Member member = memberRepository.findById(member_id).orElseThrow(MemberNotFoundException::new);
        Cart cart = cartRepository.save(new Cart(member));
        return new CartResponse(cart);
    }
}
