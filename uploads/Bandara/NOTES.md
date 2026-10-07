# Bandara N.V. — Product Catalog Management

## 1. What this module does (30-second version)
Lets merchants manage their own product listings (CRUD) and lets anyone browse a
unified catalog across every merchant — search by keyword, filter by category/price,
view product detail pages with reviews. Persona: the "Browsing shopper."

## 2. File-by-file walkthrough

### Entities (`catalog/entity/`)
- **`Product.java`** — `name`, `description`, `price` (as `BigDecimal`, never
  `double`/`float` — floating point can't represent currency exactly and accumulates
  rounding errors over many operations), `stockQuantity`, `imageUrl`, `active`
  (soft-delete flag), `flaggedForReview` (set by the *feedback* module, not by you —
  see below), a required `merchant` (`@ManyToOne`, `optional = false`), and an
  optional `category`.
- **`Category.java`** — `name` (unique) plus an optional self-referencing
  `parentCategory` for subcategory support (not necessarily used in the UI yet, but
  the data model supports it).

### Service (`catalog/service/`)
- **`ProductService.java`** — the core logic:
  - `browse(keyword, categoryId, minPrice, maxPrice)` — all four filters are
    optional (null = "don't filter on this"). Filtering happens **in Java**, after
    loading every active product with `findByActiveTrue()`, rather than building one
    dynamic SQL query. This is simpler to read/debug than Spring Data's Specification
    API, at the cost of being less efficient at large data volumes — a deliberate,
    acceptable trade-off at this project's scale. Be ready to explain *why* that
    trade-off is fine here (small dataset, academic project) but wouldn't scale to a
    real marketplace.
  - `createProduct`, `updateProduct`, `deleteProduct` — all merchant-only, and all go
    through `ownedProduct()` first, which throws `AccessDeniedForResourceException` if
    the product's `merchant.id` doesn't match the logged-in merchant's id (from
    `CurrentUserProvider`). A merchant can never edit someone else's listing, even by
    guessing a product id in the URL.
  - `deleteProduct` is a **soft delete** — sets `active=false` rather than removing
    the row. This matters because `OrderItem` keeps a foreign key to `Product`; a hard
    delete would break past order history.
  - `updateProduct` has no explicit `.save()` call — the `product` entity is
    JPA-managed inside the `@Transactional` method, so Hibernate's dirty checking
    writes the changes automatically on commit. If asked "where's the save," this is
    the answer.
- **`CategoryService.java`** — thin, just `listAll()`.

### Controllers (`catalog/controller/`)
- **`CatalogController.java`** — public browsing, `/catalog` and `/catalog/{id}`. Note
  it also pulls review data (`ReviewService`) to show ratings on the product detail
  page — a legitimate cross-module dependency on Weerawickrama's module.
- **`MerchantProductController.java`** — `/merchant/products` CRUD screens.

### Repository (`catalog/repository/`)
- **`ProductRepository.java`** — Spring Data derived queries. Note the underscore
  naming: `findByMerchant_Id(merchantId)` means "the id of this product's `merchant`
  relationship," not a literal field called `merchantId`. Same for `findByCategory_Id`.
  `findByFlaggedForReviewTrue()` powers the platform employee's flagged-products view
  in the feedback module.

## 3. Design decisions likely to come up
- **Why `BigDecimal` for price, not `double`?** Money math with floating point
  accumulates rounding errors (e.g. `0.1 + 0.2 != 0.3` in binary floating point).
  `BigDecimal` represents decimal values exactly.
- **Why filter in Java instead of a dynamic query?** Readability/debuggability over
  raw performance, acceptable at academic-project data volumes.
- **Why soft delete instead of hard delete?** Preserves referential integrity for
  historical orders that reference the product.
- **Why does `ProductService` need `CurrentUserProvider`?** Same ownership-scoping
  pattern used everywhere else in the app (see Neluvinda's `MerchantProfileService`
  for the same idea) — "which merchant" always comes from the session, never from a
  client-supplied id.

## 4. How this connects to other modules
- **`Merchant`** (Neluvinda's entity) — every `Product` requires one.
- **Cart module** (Vaas) references `Product` directly in `CartItem`.
- **Order module** (Maliduwa) snapshots `Product` price/quantity into `OrderItem` at
  checkout time — it does not keep reading live product data for historical orders.
- **Feedback module** (Weerawickrama) sets `Product.flaggedForReview` based on average
  rating, and reads/writes it through `ProductRepository`, not through your service.

## 5. Likely viva questions
- **"What stops a merchant from editing another merchant's product by changing the
  URL id?"** — `ownedProduct()` checks `product.getMerchant().getId()` against the
  current session's user id before any mutation; throws
  `AccessDeniedForResourceException` otherwise.
- **"Why is category optional on a product?"** — `@JoinColumn(name = "category_id")`
  has no `nullable = false`, and `resolveCategory()` returns `null` if no id was
  submitted — a product can exist uncategorized.
- **"How would this scale if you had 100,000 products?"** — In-Java filtering after
  loading all active products would become a bottleneck; you'd move to a database
  query (Specification API or native SQL) or a search index. Good to say this
  proactively if asked about scalability, since it demonstrates you understand the
  trade-off you made.
- **"Is there currently a check that only APPROVED merchants can create products?"**
  — Honest answer: no, not yet in `ProductService.createProduct()`. That's a real gap
  — see "try this yourself" below.

## 6. Try-this-yourself (to have something you genuinely wrote)
- Add a guard in `createProduct()`: throw a `BusinessRuleViolationException` if
  `currentMerchant().getVerificationStatus() != VerificationStatus.APPROVED`. Small,
  cross-references Neluvinda's module, and closes a real gap.
- Add a unit test for `ProductService.browse()` covering at least one filter
  combination (e.g. keyword + price range together).
- Add a `findByNameContainingIgnoreCase` usage somewhere if you want a DB-level
  keyword search instead of the in-Java `.contains()` — even just as an experiment to
  compare against the current approach, so you can talk about both.
