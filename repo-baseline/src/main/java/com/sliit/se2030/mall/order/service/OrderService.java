package com.sliit.se2030.mall.order.service;

import com.sliit.se2030.mall.cart.entity.Cart;
import com.sliit.se2030.mall.cart.entity.CartItem;
import com.sliit.se2030.mall.cart.service.CartService;
import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.order.dto.CheckoutForm;
import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.OrderItem;
import com.sliit.se2030.mall.order.entity.OrderStatus;
import com.sliit.se2030.mall.order.entity.Payment;
import com.sliit.se2030.mall.order.entity.PaymentStatus;
import com.sliit.se2030.mall.order.repository.OrderItemRepository;
import com.sliit.se2030.mall.order.repository.OrderRepository;
import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order and Payment Handling module. checkout() realizes "Checkout includes
 * Make Payment": it converts the current customer's cart into an Order +
 * OrderItems, validates/deducts stock, clears the cart, and simulates
 * payment -- all in one transaction, same pattern as MerchantVerificationService.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentService paymentService;
    private final CartService cartService;
    private final CustomerRepository customerRepository;
    private final CurrentUserProvider currentUserProvider;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                         PaymentService paymentService, CartService cartService,
                         CustomerRepository customerRepository, CurrentUserProvider currentUserProvider) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentService = paymentService;
        this.cartService = cartService;
        this.customerRepository = customerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public Order checkout(CheckoutForm form) {
        // TODO: implement checkout -- see your NOTES.md, "OrderService.checkout()".
        // Required steps, in order: load the current customer's cart items (reject
        // if empty); validate stock for every item BEFORE creating anything; create
        // the Order with a snapshotted total; for each cart item, deduct live stock,
        // create an OrderItem with price/quantity snapshots and status CONFIRMED;
        // clear the cart; simulate payment (reject if it isn't SIMULATED_SUCCESS);
        // recomputeOrderStatus(order); return the order.
        throw new UnsupportedOperationException("TODO: implement checkout()");
    }

    public List<Order> getOrderHistoryForCurrentCustomer() {
        Long customerId = currentUserProvider.getCurrentUserId();
        return orderRepository.findByCustomer_Id(customerId);
    }

    public Order getOrderDetail(Long orderId) {
        // TODO: implement getOrderDetail -- see your NOTES.md,
        // "OrderService" ownership pattern. Load the order, then throw
        // AccessDeniedForResourceException if order.getCustomer().getId() doesn't
        // match the current customer.
        throw new UnsupportedOperationException("TODO: implement getOrderDetail()");
    }

    public List<OrderItem> getItemsForOrder(Long orderId) {
        return orderItemRepository.findByOrder_Id(orderId);
    }

    public List<OrderItem> getOrderItemsForCurrentMerchant() {
        Long merchantId = currentUserProvider.getCurrentUserId();
        return orderItemRepository.findByMerchantId(merchantId);
    }

    @Transactional
    public void updateOrderItemStatus(Long orderItemId, OrderStatus newStatus) {
        // TODO: implement updateOrderItemStatus -- see your NOTES.md,
        // "OrderService.updateOrderItemStatus()". Load the item, verify
        // item.getMerchantId() matches the current merchant, set the new status,
        // then call recomputeOrderStatus(item.getOrder()).
        throw new UnsupportedOperationException("TODO: implement updateOrderItemStatus()");
    }

    // Order.status is a "weakest link" rollup of its items' statuses -- e.g. an
    // order isn't SHIPPED until every merchant's items in it are SHIPPED or further.
    private void recomputeOrderStatus(Order order) {
        // TODO: implement recomputeOrderStatus -- see your NOTES.md,
        // "OrderService.recomputeOrderStatus()" for the full rollup rule (this is
        // the single most likely thing to be asked about at viva -- read it carefully
        // rather than guessing at the logic).
        throw new UnsupportedOperationException("TODO: implement recomputeOrderStatus()");
    }
}
