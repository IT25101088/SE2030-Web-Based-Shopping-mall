package com.sliit.se2030.mall.order.repository;

import com.sliit.se2030.mall.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomer_Id(Long customerId);
}
