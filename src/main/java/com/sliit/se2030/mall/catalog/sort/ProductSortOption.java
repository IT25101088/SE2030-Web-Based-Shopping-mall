package com.sliit.se2030.mall.catalog.sort;

/**
 * The choices in the catalog's "Sort by" list, each paired with the strategy
 * that does the sorting. The controller turns the ?sort= value into one of
 * these and passes its strategy to ProductService.
 */
public enum ProductSortOption {
    NEWEST("Newest first", new NewestFirstSort()),
    PRICE_LOW_HIGH("Price: low to high", new PriceLowToHighSort()),
    PRICE_HIGH_LOW("Price: high to low", new PriceHighToLowSort()),
    NAME_A_Z("Name: A to Z", new NameAToZSort());

    private final String label;
    private final ProductSortStrategy strategy;

    ProductSortOption(String label, ProductSortStrategy strategy) {
        this.label = label;
        this.strategy = strategy;
    }

    public String getLabel() {
        return label;
    }

    public ProductSortStrategy getStrategy() {
        return strategy;
    }

    // A missing or unknown ?sort= value (e.g. an old bookmark) falls back to
    // NEWEST rather than an error page.
    public static ProductSortOption fromParam(String value) {
        if (value != null) {
            for (ProductSortOption option : values()) {
                if (option.name().equalsIgnoreCase(value)) {
                    return option;
                }
            }
        }
        return NEWEST;
    }
}
