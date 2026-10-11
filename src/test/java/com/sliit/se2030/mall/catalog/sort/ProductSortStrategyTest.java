package com.sliit.se2030.mall.catalog.sort;

import com.sliit.se2030.mall.catalog.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Plain unit test of the catalog sort strategies. */
class ProductSortStrategyTest {

    private final List<Product> products = List.of(
            new Product("banana", new BigDecimal("200"), 1, null),
            new Product("Apple", new BigDecimal("500"), 1, null),
            new Product("cherry", new BigDecimal("100"), 1, null));

    @Test
    void priceLowToHigh() {
        assertThat(new PriceLowToHighSort().sort(products)).extracting(Product::getName)
                .containsExactly("cherry", "banana", "Apple");
    }

    @Test
    void priceHighToLow() {
        assertThat(new PriceHighToLowSort().sort(products)).extracting(Product::getName)
                .containsExactly("Apple", "banana", "cherry");
    }

    @Test
    void nameAToZ_ignoresCase() {
        assertThat(new NameAToZSort().sort(products)).extracting(Product::getName)
                .containsExactly("Apple", "banana", "cherry");
    }

    @Test
    void unknownSortParam_fallsBackToNewest() {
        assertThat(ProductSortOption.fromParam("price_low_high")).isEqualTo(ProductSortOption.PRICE_LOW_HIGH);
        assertThat(ProductSortOption.fromParam("nonsense")).isEqualTo(ProductSortOption.NEWEST);
        assertThat(ProductSortOption.fromParam(null)).isEqualTo(ProductSortOption.NEWEST);
    }
}
