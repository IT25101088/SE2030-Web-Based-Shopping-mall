package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.cart.entity.Cart;
import com.sliit.se2030.mall.cart.entity.CartItem;
import com.sliit.se2030.mall.cart.service.CartService;
import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.service.ProductService;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.feedback.service.ReputationService;
import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.OrderItem;
import com.sliit.se2030.mall.order.entity.OrderStatus;
import com.sliit.se2030.mall.order.service.OrderService;
import com.sliit.se2030.mall.support.entity.Inquiry;
import com.sliit.se2030.mall.support.entity.InquiryStatus;
import com.sliit.se2030.mall.support.service.InquiryService;
import com.sliit.se2030.mall.user.dto.CustomerDashboard;
import com.sliit.se2030.mall.user.dto.EmployeeDashboard;
import com.sliit.se2030.mall.user.dto.MerchantDashboard;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Builds the numbers and short lists shown on the three role dashboards.
 *
 * It only READS, and only through other modules' existing public service
 * methods, so each module keeps its own rules (e.g. "a merchant only sees
 * their own orders") and this class just counts what it is given.
 *
 * Counting is done in Java with streams. That loads whole lists, which is fine
 * for a mall this size; for a much bigger mall we would add countBy...
 * queries to the repositories instead.
 */
@Service
public class DashboardService {

    // A product with this many or fewer left counts as "low on stock".
    public static final int LOW_STOCK_LIMIT = 5;
    // How many rows the "Needs you next" lists show.
    private static final int NEXT_UP_SIZE = 3;

    private final CartService cartService;
    private final OrderService orderService;
    private final InquiryService inquiryService;
    private final ProductService productService;
    private final ReputationService reputationService;
    private final MerchantVerificationService merchantVerificationService;
    private final CurrentUserProvider currentUserProvider;

    public DashboardService(CartService cartService, OrderService orderService, InquiryService inquiryService,
                            ProductService productService, ReputationService reputationService,
                            MerchantVerificationService merchantVerificationService,
                            CurrentUserProvider currentUserProvider) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.inquiryService = inquiryService;
        this.productService = productService;
        this.reputationService = reputationService;
        this.merchantVerificationService = merchantVerificationService;
        this.currentUserProvider = currentUserProvider;
    }

    public CustomerDashboard forCustomer() {
        Cart cart = cartService.getOrCreateCartForCurrentCustomer();
        List<CartItem> cartItems = cartService.getItems(cart);
        int cartItemCount = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

        // "On the way" = bought but not yet delivered (and not cancelled).
        List<OrderItem> orderItems = orderService.getOrderItemsForCurrentCustomer();
        long itemsOnTheWay = orderItems.stream()
                .filter(item -> item.getStatus() != OrderStatus.DELIVERED
                        && item.getStatus() != OrderStatus.CANCELLED)
                .count();

        // The list comes back newest order first, so the first item belongs to the latest order.
        Order latestOrder = orderItems.isEmpty() ? null : orderItems.get(0).getOrder();
        int latestOrderItemCount = latestOrder == null ? 0 : (int) orderItems.stream()
                .filter(item -> item.getOrder().getId().equals(latestOrder.getId()))
                .count();

        long openInquiries = inquiryService.getInquiriesForCurrentCustomer().stream()
                .filter(inquiry -> inquiry.getStatus() != InquiryStatus.RESOLVED)
                .count();

        return new CustomerDashboard(cartItemCount, cartService.calculateTotal(cart), itemsOnTheWay,
                openInquiries, latestOrder, latestOrderItemCount);
    }

    public MerchantDashboard forMerchant() {
        // "To send out" = paid for or waiting, but not shipped yet. Oldest first,
        // because those customers have waited longest.
        List<OrderItem> toSend = orderService.getOrderItemsForCurrentMerchant().stream()
                .filter(item -> item.getStatus() == OrderStatus.PENDING
                        || item.getStatus() == OrderStatus.CONFIRMED)
                .sorted(Comparator.comparing(OrderItem::getCreatedAt))
                .toList();

        List<Product> products = productService.listOwnProducts();
        long hiddenProducts = products.stream().filter(product -> !product.isActive()).count();
        // Hidden products aren't for sale, so running low on them doesn't matter.
        long lowStockProducts = products.stream()
                .filter(product -> product.isActive() && product.getStockQuantity() <= LOW_STOCK_LIMIT)
                .count();

        // This list only holds requests currently with this shop and not yet answered.
        long openInquiries = inquiryService.getInquiriesForCurrentMerchant().size();

        // Ratings are 1 to 5 stars, so 0.0 can only mean "no reviews yet".
        // Rounded to one decimal place here (e.g. 4.6), so the page can print it as it is.
        double score = reputationService.getMerchantReputationScore(currentUserProvider.getCurrentUserId());
        Double rating = score > 0 ? Math.round(score * 10) / 10.0 : null;

        return new MerchantDashboard(toSend.size(), toSend.stream().limit(NEXT_UP_SIZE).toList(),
                products.size(), hiddenProducts, lowStockProducts, openInquiries, rating);
    }

    public EmployeeDashboard forEmployee() {
        long shopsToApprove = merchantVerificationService.listPending().size();

        // Of the unresolved help requests, only OPEN (new) and AWAITING_REVIEW
        // (a shop has answered) need the mall team to act. IN_PROGRESS ones
        // are with a shop or already replied to.
        List<Inquiry> unresolved = inquiryService.listOpenInquiries();
        List<Inquiry> waiting = unresolved.stream()
                .filter(inquiry -> inquiry.getStatus() == InquiryStatus.OPEN
                        || inquiry.getStatus() == InquiryStatus.AWAITING_REVIEW)
                .sorted(Comparator.comparing(Inquiry::getCreatedAt))
                .toList();
        long withShops = unresolved.stream().filter(inquiry -> inquiry.getMerchant() != null).count();

        // A product can be on both flagged lists, so count each product once.
        long flaggedProducts = Stream.concat(productService.listFlaggedByAdmin().stream(),
                        productService.listFlaggedForLowRating().stream())
                .map(Product::getId)
                .distinct()
                .count();

        return new EmployeeDashboard(shopsToApprove, waiting.size(), withShops,
                waiting.stream().limit(NEXT_UP_SIZE).toList(), flaggedProducts);
    }
}
