# Maliduwa M.G.B.B. — Order and Payment Handling

## 1. What this module does (30-second version)
Converts a customer's cart into an order, simulates payment (no real gateway — out of
scope per the project docs), tracks per-item fulfillment status across multiple
merchants in one order, and gives customers order history + merchants an
order-management view. Persona: the "Frequent buyer."

This is the busiest module — it depends on cart, catalog, and user, and feedback
depends on it. Budget the most viva-prep time here.

## 2. File-by-file walkthrough

### Entities (`order/entity/`)
- **`Order.java`** — table is named `orders`, not `order`, because `ORDER` is a
  reserved SQL keyword (as in `ORDER BY`) — good one-liner if asked "why the odd table
  name." Holds a `customer`, an overall `status` (`OrderStatus`), a `totalAmount`
  **snapshot** (never recalculated from live prices later), and a `shippingAddress`
  **snapshot** (a copy, not a live link to the customer's address — if they edit their
  address later, past orders must still show what was actually shipped to).
- **`OrderItem.java`** — one row per product per order. Key fields:
  - `merchantId` — a **denormalized copy** of `product.merchant.id` at order time.
    This lets a merchant query "my order items" directly (`findByMerchantId`) without
    joining through `Product`, and it survives even if the product is later deleted.
  - `quantitySnapshot`, `unitPriceSnapshot` — frozen at checkout time; a later price
    change must never rewrite historical order data.
  - `status` (`OrderStatus`) — **per-item**, not per-order. This is the crux of the
    module: since one order can span multiple merchants, each merchant only ever
    advances the fulfillment status of *their own* items.
- **`OrderStatus.java`** — `PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`,
  `CANCELLED`.
- **`Payment.java`** — `@OneToOne` to `Order`, with `status` (simulated only — see
  `PaymentStatus`), `paymentMethod`, and a `transactionRef` (a random UUID standing in
  for a real gateway's reference number).

### Services (`order/service/`)
- **`OrderService.checkout(form)`** — the heart of the module, all in one
  `@Transactional` method:
  1. Loads the customer's cart items; throws `BusinessRuleViolationException` if
     empty.
  2. **Validates stock** for every item (`cartItem.quantity > product.stockQuantity`)
     *before* creating anything — this is the actual stock-safety check for the whole
     app, not something Vaas's cart module does.
  3. Creates the `Order` with a total computed from the cart.
  4. For each cart item: **deducts stock** from the live `Product`, creates an
     `OrderItem` with price/quantity snapshots, and sets its status to `CONFIRMED`
     (not `PENDING`) — because payment is simulated and always succeeds, there's no
     real "awaiting payment" state to represent.
  5. Clears the cart (`cartService.clearCart(cart)`).
  6. Calls `paymentService.simulatePayment(order)`; if that ever returned anything
     other than `SIMULATED_SUCCESS` it would throw — in practice this never happens
     since simulation always succeeds, but the check is there for when/if a real
     gateway replaces it.
  7. Calls `recomputeOrderStatus(order)`.
  - **`recomputeOrderStatus()`** — this is the "weakest link" rollup logic: the
    order's overall status is derived from *all* its items' statuses. E.g. the order
    isn't `SHIPPED` until every merchant's items in it are `SHIPPED` or further; it's
    `CANCELLED` only if every item is cancelled. Read this method carefully — it's the
    single most likely thing to be asked about in viva, since it's the non-obvious
    business rule unique to a multi-merchant order.
  - **`updateOrderItemStatus()`** — a merchant can only update items where
    `item.getMerchantId()` matches their own id (ownership check), and every update
    triggers `recomputeOrderStatus()` on the parent order.
- **`PaymentService.simulatePayment()`** — always returns `SIMULATED_SUCCESS` with a
  random `transactionRef`. Deliberately simple — no real payment gateway integration
  is in scope for this project.

### Controllers (`order/controller/`)
- **`CheckoutController.java`** — `GET /checkout` (review cart + shipping form),
  `POST /checkout` (submit → creates the order, redirects to order detail).
- **`CustomerOrderController.java`** — `/orders` (history), `/orders/{id}` (detail).
- **`MerchantOrderController.java`** — `/merchant/orders` lists a merchant's own
  order *items* (not whole orders — a merchant never sees another merchant's line
  items in a shared order), and lets them update one item's status at a time.

## 3. Design decisions likely to come up
- **Why snapshot price/quantity/address instead of always reading live data?**
  Historical accuracy — an order must forever reflect what was actually agreed at
  purchase time, even if the product's price or the customer's address changes later.
- **Why is `Order.status` a computed rollup rather than something a merchant sets
  directly?** Because one order can span several merchants; there's no single actor
  who could correctly set the whole order's status, so it's derived from the items
  instead.
- **Why does stock validation happen at checkout, not earlier (e.g. at add-to-cart)?**
  Stock can change between adding to cart and checking out (someone else buys it);
  checking once, right before committing, is the only point where the check is
  actually meaningful.
- **Why is stock validated *before* any `Order`/`OrderItem` is created, in a separate
  loop?** So the whole checkout fails cleanly (nothing is created) rather than
  partially creating some order items and then failing partway through.

## 4. How this connects to other modules
- **Depends on:** `Cart`/`CartItem` (Vaas) to source what's being ordered, `Product`
  (Bandara) for price/stock, `Customer` (Neluvinda) for who's ordering.
- **Depended on by:** the feedback module (Weerawickrama) — a `Review` requires a
  `verifiedOrderItem` that must be `DELIVERED`, which is `OrderStatus` from this
  module. If you change `OrderStatus` values, you'll break Weerawickrama's
  verified-purchase check — flag any such change to them.

## 5. Likely viva questions
- **"What happens if a customer's order has items from three different merchants and
  two have shipped but one hasn't even been confirmed?"** — Walk through
  `recomputeOrderStatus()`: `allShippedOrBeyond` would be false (the unconfirmed item
  fails that check), `allConfirmedOrBeyond` would also be false (an item is still
  `PENDING`... actually re-check: `CONFIRMED` counts as beyond `PENDING`). Trace it
  live with the actual enum values rather than reciting this — that's what
  demonstrates real understanding.
- **"Why `CONFIRMED` and not `PENDING` right after checkout?"** — Because payment is
  simulated and always succeeds instantly; there's no real "awaiting payment"
  window to represent with `PENDING`.
- **"What stops a merchant from marking another merchant's item as shipped?"** —
  `updateOrderItemStatus()` checks `item.getMerchantId()` against the current
  session's user id and throws `AccessDeniedForResourceException` otherwise.
- **"Is there a race condition between two customers buying the last unit of a
  product?"** — Honest answer: yes, potentially. The stock check and the stock
  deduction happen inside one `@Transactional` method, but without an explicit
  pessimistic lock or optimistic version check on `Product`, two concurrent checkouts
  reading the same stock count before either commits could both pass validation. Worth
  naming this as a known limitation rather than claiming it's fully race-safe.

## 6. Try-this-yourself (to have something you genuinely wrote)
- Add a `CANCELLED` transition guard: prevent a merchant from setting an item back to
  an earlier status once it's `DELIVERED` (currently `updateOrderItemStatus` allows any
  status transition). Small, real, testable.
- Add a unit test for `recomputeOrderStatus()` covering the "mixed statuses across
  merchants" case described above.
- Add optimistic locking (`@Version` on `Product`) to address the race condition
  mentioned above — a great "future work" talking point even if you don't fully wire
  it up.
