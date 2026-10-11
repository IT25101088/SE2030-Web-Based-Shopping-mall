package com.sliit.se2030.mall.catalog.sort;

import com.sliit.se2030.mall.catalog.entity.Product;

import java.util.List;

/**
 * Strategy pattern -- the Strategy interface. Each way of ordering the catalog
 * is its own class; ProductService.browse() just hands its filtered list to
 * whichever one the shopper picked.
 */
public interface ProductSortStrategy {

    // Returns a new sorted list; the list passed in is left as it is.
    List<Product> sort(List<Product> products);
}
