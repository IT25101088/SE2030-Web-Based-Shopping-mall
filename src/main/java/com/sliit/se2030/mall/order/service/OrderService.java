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
        Long customerId = currentUserProvider.getCurrentUserId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        Cart cart = cartService.getOrCreateCartForCurrentCustomer();
        List<CartItem> cartItems = cartService.getItems(cart);
        if (cartItems.isEmpty()) {
            throw new BusinessRuleViolationException("Your cart is empty.");
        }

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new BusinessRuleViolationException(
                        "Not enough stock for " + product.getName() + ": only " + product.getStockQuantity()
                                + " left.");
            }
        }

        BigDecimal total = cartService.calculateTotal(cart);
        Order order = orderRepository.save(new Order(customer, total, form.getShippingAddress()));

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem(order, product, product.getMerchant().getId(),
                    cartItem.getQuantity(), product.getPrice());
            // Payment is simulated below and always succeeds, so items start out
            // CONFIRMED (paid, awaiting fulfillment) rather than PENDING.
            orderItem.setStatus(OrderStatus.CONFIRMED);
            orderItemRepository.save(orderItem);
        }

        cartService.clearCart(cart);

        Payment payment = paymentService.simulatePayment(order);
        if (payment.getStatus() != PaymentStatus.SIMULATED_SUCCESS) {
            throw new BusinessRuleViolationException("Payment failed. Please try again.");
        }

        recomputeOrderStatus(order);
        return order;
    }

    public List<Order> getOrderHistoryForCurrentCustomer() {
        Long customerId = currentUserProvider.getCurrentUserId();
        return orderRepository.findByCustomer_Id(customerId);
    }

    public Order getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        Long customerId = currentUserProvider.getCurrentUserId();
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new AccessDeniedForResourceException("This order does not belong to you.");
        }
        return order;
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
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found: " + orderItemId));
        Long merchantId = currentUserProvider.getCurrentUserId();
        if (!item.getMerchantId().equals(merchantId)) {
            throw new AccessDeniedForResourceException("This order item does not belong to you.");
        }
        item.setStatus(newStatus);
        recomputeOrderStatus(item.getOrder());
    }

    // Order.status is a "weakest link" rollup of its items' statuses -- e.g. an
    // order isn't SHIPPED until every merchant's items in it are SHIPPED or further.
    private void recomputeOrderStatus(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrder_Id(order.getId());
        boolean allCancelled = items.stream().allMatch(i -> i.getStatus() == OrderStatus.CANCELLED);
        boolean allDelivered = items.stream().allMatch(i -> i.getStatus() == OrderStatus.DELIVERED);
        boolean allShippedOrBeyond = items.stream()
                .allMatch(i -> i.getStatus() == OrderStatus.SHIPPED || i.getStatus() == OrderStatus.DELIVERED);
        boolean allConfirmedOrBeyond = items.stream()
                .allMatch(i -> i.getStatus() != OrderStatus.PENDING && i.getStatus() != OrderStatus.CANCELLED);

        if (allCancelled) {
            order.setStatus(OrderStatus.CANCELLED);
        } else if (allDelivered) {
            order.setStatus(OrderStatus.DELIVERED);
        } else if (allShippedOrBeyond) {
            order.setStatus(OrderStatus.SHIPPED);
        } else if (allConfirmedOrBeyond) {
            order.setStatus(OrderStatus.CONFIRMED);
        } else {
            order.setStatus(OrderStatus.PENDING);
        }
    }
}
