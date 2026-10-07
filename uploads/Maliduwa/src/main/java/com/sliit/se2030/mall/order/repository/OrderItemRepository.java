package com.sliit.se2030.mall.order.repository;

import com.sliit.se2030.mall.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_Id(Long orderId);

    // merchantId is a plain column (not a relationship), so no underscore needed here.
    List<OrderItem> findByMerchantId(Long merchantId);
}
