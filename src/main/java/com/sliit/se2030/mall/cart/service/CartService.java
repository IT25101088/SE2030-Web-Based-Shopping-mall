package com.sliit.se2030.mall.cart.service;

import com.sliit.se2030.mall.cart.entity.Cart;
import com.sliit.se2030.mall.cart.entity.CartItem;
import com.sliit.se2030.mall.cart.repository.CartItemRepository;
import com.sliit.se2030.mall.cart.repository.CartRepository;
import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Shopping Cart Handling module. Every method scopes to the logged-in
 * customer via CurrentUserProvider -- never a cart/customer id from the
 * client -- same ownership pattern as MerchantProfileService.
 */
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CurrentUserProvider currentUserProvider;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                        ProductRepository productRepository, CustomerRepository customerRepository,
                        CurrentUserProvider currentUserProvider) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public Cart getOrCreateCartForCurrentCustomer() {
        Long customerId = currentUserProvider.getCurrentUserId();
        return cartRepository.findByCustomer_Id(customerId)
                .orElseGet(() -> {
                    Customer customer = customerRepository.findById(customerId)
                            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
                    return cartRepository.save(new Cart(customer));
                });
    }

    public List<CartItem> getItems(Cart cart) {
        return cartItemRepository.findByCart_Id(cart.getId());
    }

    @Transactional
    public void addItem(Long productId, int quantity) {
        Cart cart = getOrCreateCartForCurrentCustomer();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .ifPresentOrElse(
                        existing -> existing.setQuantity(existing.getQuantity() + quantity),
                        () -> cartItemRepository.save(new CartItem(cart, product, quantity)));
    }

    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = ownedItem(cartItemId);
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
        }
    }

    @Transactional
    public void removeItem(Long cartItemId) {
        cartItemRepository.delete(ownedItem(cartItemId));
    }

    // Called by OrderService once checkout has converted these items into an Order.
    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteAll(getItems(cart));
    }

    // Computed on read, never stored -- avoids a stale total if a product's price changes later.
    public BigDecimal calculateTotal(Cart cart) {
        return getItems(cart).stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CartItem ownedItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));
        Long currentCustomerId = currentUserProvider.getCurrentUserId();
        if (!item.getCart().getCustomer().getId().equals(currentCustomerId)) {
            throw new AccessDeniedForResourceException("This cart item does not belong to you.");
        }
        return item;
    }
}
