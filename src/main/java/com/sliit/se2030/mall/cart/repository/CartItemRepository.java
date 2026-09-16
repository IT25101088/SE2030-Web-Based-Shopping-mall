package com.sliit.se2030.mall.cart.repository;

import com.sliit.se2030.mall.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart_Id(Long cartId);

    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
}
