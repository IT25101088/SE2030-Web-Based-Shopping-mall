# Vaas W.P.S.D. — Shopping Cart Handling

## 1. What this module does (30-second version)
Lets a logged-in customer add/update/remove items in their cart and see a running
total, persisted across logins (not just a browser tab). Persona: the "Multi-item
shopper."

## 2. File-by-file walkthrough

### Entities (`cart/entity/`)
- **`Cart.java`** — one cart per customer. `@OneToOne` to `Customer`, tied to the
  customer's *account* (not the HTTP session) — that's why the cart survives logging
  out and back in on a different device, unlike a session-scoped cart would.
- **`CartItem.java`** — links a `Cart` to a `Product` with a `quantity`. Has a
  **unique constraint on `(cart_id, product_id)`** — this is the mechanism that
  guarantees adding the same product twice bumps the existing row's quantity instead
  of creating a duplicate row. Worth knowing this is enforced at the DB level, not
  just in application code.

### Service (`cart/service/CartService.java`)
- **`getOrCreateCartForCurrentCustomer()`** — looks up the cart by the current
  session's customer id (`CurrentUserProvider`); if none exists yet, creates one. This
  is the "lazy cart creation" pattern — a `Cart` row doesn't exist until the customer's
  first add-to-cart action.
- **`addItem(productId, quantity)`** — uses
  `cartItemRepository.findByCart_IdAndProduct_Id(...).ifPresentOrElse(...)`: if the
  product's already in the cart, it **increments** the existing row's quantity; only
  creates a new `CartItem` row if it's genuinely new. This is what makes the unique
  constraint above meaningful in practice, not just a DB safety net.
- **`updateQuantity(cartItemId, quantity)`** — if the new quantity is `<= 0`, it
  **deletes** the item rather than saving a zero-quantity row. Zero items in a cart
  means the item isn't in the cart.
- **`removeItem`** / **`clearCart`** — `clearCart` is called by `OrderService`
  (Maliduwa's module) once checkout has converted the cart into an order — you don't
  call it yourself from this module.
- **`calculateTotal(cart)`** — computed fresh on every read by summing
  `product.price * quantity` across items, **never stored**. This avoids a stale total
  if a product's price changes after being added to the cart but before checkout.
- **`ownedItem(cartItemId)`** — the same ownership-check pattern as every other
  module: throws `AccessDeniedForResourceException` if the cart item's cart doesn't
  belong to the current customer. Prevents one customer from updating/removing another
  customer's cart item by guessing an id.

### Controller (`cart/controller/CartController.java`)
- `GET /cart` — view cart + total.
- `POST /cart/add` — add item; on validation failure, redirects back to `/catalog`
  (not back to a cart-specific error page — worth noting if asked about UX).
- `POST /cart/{itemId}/update`, `POST /cart/{itemId}/remove`.

## 3. Design decisions likely to come up
- **Why tie the cart to the customer account instead of the HTTP session?** A
  session-scoped cart disappears when the session expires or the customer switches
  devices. Tying it to the account means "add to cart on your phone, check out on your
  laptop" works, and matches the backlog item "cart persistence across session."
- **Why recompute the total on every read instead of storing it on the `Cart`
  entity?** Storing it risks going stale if a merchant changes a product's price while
  it's sitting in someone's cart. Computing fresh guarantees correctness at the cost
  of a small amount of extra computation — a good trade at this scale.
- **Why a DB unique constraint on `(cart_id, product_id)` instead of relying purely on
  the `ifPresentOrElse` check in `addItem`?** Belt-and-braces: if two requests somehow
  raced (e.g. a double-click), the constraint prevents a duplicate row even if the
  application-level check missed the race.

## 4. How this connects to other modules
- **`Product`** (Bandara's entity) — every `CartItem` references one; if a merchant
  changes stock or price, the cart reflects it live (since price isn't snapshotted
  until checkout).
- **`Customer`** (Neluvinda's entity) — every `Cart` belongs to exactly one.
- **`OrderService`** (Maliduwa) reads `cartService.getItems()` and
  `cartService.calculateTotal()` to build an order, then calls `cartService.clearCart()`
  once the order is created — this is the actual "Checkout includes Make Payment"
  hand-off point in the use case diagram.

## 5. Likely viva questions
- **"What happens if I add a product, someone else buys the last unit, then I check
  out?"** — Cart doesn't check stock; `OrderService.checkout()` does, and throws a
  `BusinessRuleViolationException` if `cartItem.quantity > product.stockQuantity` at
  checkout time. So the cart can briefly hold more than is actually available — that's
  intentional and cheap; the real check happens once, at the point that matters.
- **"Why does `updateQuantity` delete the row instead of just setting quantity to
  0?"** — A cart item with quantity 0 isn't meaningfully "in the cart" — cleaner data,
  and avoids every other query needing to filter out zero-quantity rows.
- **"Could two browser tabs adding the same item at the same time create two
  `CartItem` rows?"** — The DB unique constraint on `(cart_id, product_id)` would
  reject the second insert; worth acknowledging the application code doesn't currently
  catch that specific constraint violation gracefully (it would surface as a raw DB
  error) — a known, honest limitation.

## 6. Try-this-yourself (to have something you genuinely wrote)
- Add a stock check in `addItem()` itself (not just at checkout) — throw or show a
  message if the requested quantity exceeds `product.getStockQuantity()` at
  add-to-cart time, not just at checkout. Small, real, testable.
- Add a "max quantity per item" cap (e.g. business rule: no more than 10 of one
  product per cart) — touches `CartService` and maybe `AddToCartForm` validation.
- Write a unit test for `CartService.addItem()` that verifies adding the same product
  twice increments quantity rather than creating a second row.
