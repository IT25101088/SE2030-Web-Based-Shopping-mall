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
        // TODO: implement getOrCreateCartForCurrentCustomer -- see your NOTES.md,
        // "CartService.getOrCreateCartForCurrentCustomer()" (lazy cart creation:
        // look up by current customer id, create+save one if none exists yet).
        throw new UnsupportedOperationException("TODO: implement getOrCreateCartForCurrentCustomer()");
    }

    public List<CartItem> getItems(Cart cart) {
        return cartItemRepository.findByCart_Id(cart.getId());
    }

    @Transactional
    public void addItem(Long productId, int quantity) {
        // TODO: implement addItem -- see your NOTES.md, "CartService.addItem()".
        // If the product is already in the cart, increment the existing CartItem's
        // quantity; only create a new row if it's genuinely new.
        throw new UnsupportedOperationException("TODO: implement addItem()");
    }

    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {
        // TODO: implement updateQuantity -- see your NOTES.md,
        // "CartService.updateQuantity()". A quantity <= 0 should DELETE the item,
        // not save a zero-quantity row.
        throw new UnsupportedOperationException("TODO: implement updateQuantity()");
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
        // TODO: implement calculateTotal -- see your NOTES.md,
        // "CartService.calculateTotal()". Sum product.price * quantity across
        // getItems(cart); compute fresh every call, never store the total.
        throw new UnsupportedOperationException("TODO: implement calculateTotal()");
    }

    private CartItem ownedItem(Long cartItemId) {
        // TODO: implement ownedItem -- see your NOTES.md, "CartService.ownedItem()".
        // Load the item, then throw AccessDeniedForResourceException if
        // item.getCart().getCustomer().getId() doesn't match the current customer.
        throw new UnsupportedOperationException("TODO: implement ownedItem()");
    }
}
