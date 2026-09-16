package com.sliit.se2030.mall.cart.repository;

import com.sliit.se2030.mall.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomer_Id(Long customerId);
}
