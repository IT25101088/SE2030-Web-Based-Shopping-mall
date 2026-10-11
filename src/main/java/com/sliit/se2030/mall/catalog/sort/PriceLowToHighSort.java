package com.sliit.se2030.mall.catalog.sort;

import com.sliit.se2030.mall.catalog.entity.Product;

import java.util.Comparator;
import java.util.List;

/** Strategy pattern -- a Concrete Strategy: cheapest first. */
public class PriceLowToHighSort implements ProductSortStrategy {

    @Override
    public List<Product> sort(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice).thenComparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
