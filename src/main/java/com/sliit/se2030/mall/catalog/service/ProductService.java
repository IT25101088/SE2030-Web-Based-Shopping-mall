package com.sliit.se2030.mall.catalog.service;

import com.sliit.se2030.mall.catalog.dto.ProductForm;
import com.sliit.se2030.mall.catalog.entity.Category;
import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.repository.CategoryRepository;
import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final CurrentUserProvider currentUserProvider;

    public ProductService(ProductRepository productRepository,
                           CategoryRepository categoryRepository,
                           MerchantRepository merchantRepository,
                           CurrentUserProvider currentUserProvider) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
        this.currentUserProvider = currentUserProvider;
    }

    // Public catalog browsing: keyword, categoryId and the price bounds are all
    // optional -- a null value for any of them means "don't filter on this."
    // Filtering happens in Java, after loading every active product, rather than
    // as a single dynamic SQL query. Simpler to read/debug than Spring Data's
    // Specification API, at the cost of being less efficient at large data volumes
    // -- an acceptable trade for this project's scale.
    public List<Product> browse(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        String normalizedKeyword = keyword == null ? null : keyword.toLowerCase();
        return productRepository.findByActiveTrue().stream()
                .filter(p -> normalizedKeyword == null || p.getName().toLowerCase().contains(normalizedKeyword))
                .filter(p -> categoryId == null
                        || (p.getCategory() != null && p.getCategory().getId().equals(categoryId)))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }

    public Product getProductDetail(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    public List<Product> listOwnProducts() {
        Long merchantId = currentUserProvider.getCurrentUserId();
        return productRepository.findByMerchant_Id(merchantId);
    }

    public Product createProduct(ProductForm form) {
        Merchant merchant = currentMerchant();
        if (merchant.getVerificationStatus() != VerificationStatus.APPROVED) {
            throw new BusinessRuleViolationException(
                    "Your shop is not approved yet. An admin must approve your merchant account before you can list products.");
        }
        Product product = new Product(form.getName(), form.getPrice(), form.getStockQuantity(), merchant);
        product.setDescription(form.getDescription());
        product.setImageUrl(form.getImageUrl());
        product.setCategory(resolveCategory(form.getCategoryId()));
        return productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductForm form) {
        Product product = ownedProduct(productId);
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setStockQuantity(form.getStockQuantity());
        product.setImageUrl(form.getImageUrl());
        product.setCategory(resolveCategory(form.getCategoryId()));
        // No explicit productRepository.save() call needed: `product` is a JPA-managed
        // entity (loaded in this same @Transactional method), so Hibernate's dirty
        // checking writes the changes back to the DB automatically when the
        // transaction commits.
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = ownedProduct(productId);
        // Soft delete: hide from the catalog rather than removing the row, so past
        // orders that reference this product (OrderItem FK) keep working.
        product.setActive(false);
    }

    private Product ownedProduct(Long productId) {
        Product product = getProductDetail(productId);
        Long currentMerchantId = currentUserProvider.getCurrentUserId();
        if (!product.getMerchant().getId().equals(currentMerchantId)) {
            throw new AccessDeniedForResourceException("This product does not belong to you.");
        }
        return product;
    }

    private Merchant currentMerchant() {
        Long merchantId = currentUserProvider.getCurrentUserId();
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found: " + merchantId));
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
    }
}
