# Weerawickrama K.K. — Product Feedback & Reputation Analytics

## 1. What this module does (30-second version)
Elevated beyond basic CRUD reviews: verified-purchase-only reviews, rating
distribution per product, an aggregated merchant reputation score, a merchant feedback
dashboard, merchant responses to reviews, and automatic flagging of low-rated products
for platform employee attention. Persona: the "Engaged reviewer."

## 2. File-by-file walkthrough

### Entities (`feedback/entity/`)
- **`Review.java`** — the key design choice is `verifiedOrderItem`
  (`@ManyToOne`, `optional = false`, `nullable = false`) plus a **unique constraint on
  `(customer_id, verified_order_item_id)`**. This is the actual enforcement mechanism
  for "verified-purchase-only reviews": a review can only ever be created by pointing
  at a specific `OrderItem` the customer actually bought, and the DB itself won't
  allow the same customer to review the same order item twice. The service layer adds
  the remaining checks (ownership + delivered status — see below).
- **`MerchantResponse.java`** — `@OneToOne` to `Review` (one response per review,
  enforced by a unique `review_id` column), plus `respondedBy` (which `Merchant`
  wrote it). The comment in the file flags that the service layer must check
  `respondedBy` equals `review.getProduct().getMerchant()` — a merchant can only
  respond to reviews on their *own* products, not competitors'.

### Services (`feedback/service/`)
- **`ReviewService.submitReview(form)`** — the full verified-purchase chain, in order:
  1. `getReviewableOrderItem()` — loads the `OrderItem` and checks it belongs to the
     current customer (`AccessDeniedForResourceException` otherwise). Called both by
     the GET (to show what's being reviewed) and the POST (re-validated, never trusted
     from the link alone).
  2. Checks `orderItem.getStatus() == OrderStatus.DELIVERED` — you can't review
     something that hasn't arrived yet.
  3. Checks `reviewRepository.existsByCustomer_IdAndVerifiedOrderItem_Id(...)` — one
     review per purchased item (backed by the DB unique constraint too, belt-and-braces).
  4. Saves the review, then calls `recomputeFlagForProduct()`.
  - **`recomputeFlagForProduct()`** — recalculates the product's average rating and
    sets `product.flaggedForReview = average < 2.5` **on every new review**, not just
    when it crosses the threshold downward. This means a product can be un-flagged
    later if enough good reviews come in and pull the average back up — a deliberate
    choice (not a one-way ratchet).
  - `getRatingDistribution(productId)` — builds a `1..5 -> count` map, always including
    all five keys even if some ratings have zero reviews (so the JSP can render a full
    bar chart without missing bars).
- **`ReputationService.getMerchantReputationScore(merchantId)`** — returns `0.0`
  (never `null`) when a merchant has no reviews yet, specifically so the JSP can
  render it directly without a null check.
- **`MerchantResponseService.respondToReview()`** — enforces the two rules from the
  entity comment: the review must be for one of the merchant's own products, and a
  review can only be responded to once (`BusinessRuleViolationException` on a second
  attempt).

### Controllers (`feedback/controller/`)
- **`ReviewController.java`** — `/reviews/submit` GET/POST, reachable only from a
  customer's own delivered order item.
- **`MerchantFeedbackController.java`** — `/merchant/feedback` dashboard. Note how it
  pre-builds a `Map<reviewId, MerchantResponse>` in the controller rather than handing
  the JSP a service to call per review — keeps the JSP's expression language simple
  (`${responses[review.id]}`) and avoids the JSP having to reason about an `Optional`.
- **`EmployeeFlaggedProductController.java`** — `/employee/flagged-products`, a thin
  read-only view over `productRepository.findByFlaggedForReviewTrue()`.

## 3. Design decisions likely to come up
- **Why is "verified purchase" enforced by a foreign key to `OrderItem`, not just a
  boolean flag on `Review`?** A boolean is just a claim; requiring an actual
  `OrderItem` reference means the review is *structurally* tied to a specific
  purchase, and the DB unique constraint prevents reviewing that same purchase twice.
  This is a stronger guarantee than "we checked at write time and trust it stays true."
- **Why is the low-rating flag recomputed both ways (can flag AND un-flag)?** Fairness
  — a merchant who improves after a bad review shouldn't stay permanently flagged.
  The alternative (one-way ratchet) would be simpler code but a worse system design.
- **Why does `ReputationService` return `0.0` instead of `null` or `Optional`?**
  Purely to keep the JSP simple — no null-check boilerplate in the view layer for a
  merchant with zero reviews.

## 4. How this connects to other modules
- **Depends on:** `OrderItem`/`OrderStatus` (Maliduwa) for the verified-purchase
  check — if Maliduwa changes `OrderStatus` values or the meaning of `DELIVERED`, this
  module's core business rule breaks. Also depends on `Product` (Bandara) — reviews
  attach to it, and this module writes back to `Product.flaggedForReview`.
- **Depended on by:** `CatalogController` (Bandara) reads
  `reviewService.getReviewsForProduct()` and `getAverageRating()` to show ratings on
  the public product detail page. `EmployeeFlaggedProductController` (this module)
  reads `ProductRepository.findByFlaggedForReviewTrue()` directly rather than through
  `ProductService` — worth knowing that's a direct repository dependency, not routed
  through Bandara's service.

## 5. Likely viva questions
- **"Could a customer review a product they never bought?"** — No: the DB schema
  requires `verified_order_item_id` to be non-null and to reference a real
  `OrderItem`, and the service checks that item belongs to the reviewing customer and
  is `DELIVERED`. There's no code path that creates a `Review` without going through
  `submitReview()`.
- **"What's the flagging threshold and why 2.5?"** — `LOW_RATING_THRESHOLD = 2.5` in
  `ReviewService`, a hardcoded constant. Honest answer if pushed on "why 2.5
  specifically": it's a reasonable midpoint on a 5-star scale chosen for the project
  scope, not derived from real marketplace data — a legitimate place to say "this
  would be tuned with real data in production."
- **"Can a merchant respond to a review twice, or edit their response?"** — No to
  both currently: `respondToReview()` throws if a response already exists, and there's
  no update/edit path. Worth naming as a possible future improvement.
- **"What happens to a product's flag if all its reviews are deleted?"** — There's no
  review-deletion feature at all right now, so this can't currently happen — fine to
  say so directly if asked, rather than guessing.

## 6. Try-this-yourself (to have something you genuinely wrote)
- Make `LOW_RATING_THRESHOLD` configurable (e.g. via `application.properties`) instead
  of a hardcoded constant — small, real, and a good "why would you do this" talking
  point (testability, tuning without a redeploy).
- Add a unit test for `recomputeFlagForProduct()` covering both directions: a product
  crossing below 2.5 gets flagged, and one recovering above 2.5 gets un-flagged.
- Add an edit capability to `MerchantResponseService` (currently respond-once-only) —
  touches entity, service, and controller, a good end-to-end change to describe.
