package com.sliit.se2030.mall.order.repository;

import com.sliit.se2030.mall.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_Id(Long orderId);
}
