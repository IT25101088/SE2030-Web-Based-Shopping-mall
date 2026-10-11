package com.sliit.se2030.mall.catalog.service;

import com.sliit.se2030.mall.catalog.dto.ProductForm;
import com.sliit.se2030.mall.catalog.entity.Category;
import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.repository.CategoryRepository;
import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.user.entity.Merchant;
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
        // TODO: implement browse -- see your NOTES.md, "ProductService.browse()".
        // All four filters are optional (null = "don't filter on this"); filter
        // findByActiveTrue() in Java rather than a dynamic query.
        throw new UnsupportedOperationException("TODO: implement browse()");
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
        // TODO: implement createProduct -- see your NOTES.md,
        // "ProductService.createProduct()" (build a Product from the form for
        // currentMerchant(), resolving the category, and save it).
        throw new UnsupportedOperationException("TODO: implement createProduct()");
    }

    @Transactional
    public void updateProduct(Long productId, ProductForm form) {
        // TODO: implement updateProduct -- see your NOTES.md,
        // "ProductService.updateProduct()". Load via ownedProduct(), mutate fields;
        // no explicit save() needed since the entity is JPA-managed (dirty checking).
        throw new UnsupportedOperationException("TODO: implement updateProduct()");
    }

    @Transactional
    public void deleteProduct(Long productId) {
        // TODO: implement deleteProduct -- see your NOTES.md,
        // "ProductService.deleteProduct()". This is a SOFT delete (active=false),
        // never remove the row -- OrderItem keeps a foreign key to Product.
        throw new UnsupportedOperationException("TODO: implement deleteProduct()");
    }

    private Product ownedProduct(Long productId) {
        // TODO: implement ownedProduct -- see your NOTES.md, "ProductService.ownedProduct()".
        // Load the product, then throw AccessDeniedForResourceException if
        // product.getMerchant().getId() doesn't match the current session's user id.
        throw new UnsupportedOperationException("TODO: implement ownedProduct()");
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
