# Shopping Mall — Setup & Team Notes

SE2030 group project: web-based multi-merchant shopping mall. Java 17, Spring Boot, JSP, Spring Data JPA, MySQL, Maven.

## Running locally

1. Prerequisites: JDK 17, a running MySQL server.
2. Create the database (or let the app do it — the JDBC URL has `createDatabaseIfNotExist=true`):
   ```
   CREATE DATABASE shopping_mall;
   ```
3. Check `src/main/resources/application.properties` matches your local MySQL username/password.
4. Run: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`) — no local Maven install needed, the wrapper downloads it on first run.
5. App runs at `http://localhost:8080`. A platform employee account is seeded automatically on first boot: `admin@mall.local` / `admin123` (see `DataSeedConfig`).

## Project layout

Package-by-feature under `com.sliit.se2030.mall`: `common/`, `config/`, and `security/` are shared; `user/`, `catalog/`, `cart/`, `order/`, `feedback/`, `support/` are one package per core function, each owned by one team member. Every feature package follows the same shape: `entity/ repository/ service/ controller/ dto/`. Copy that shape for your own module — don't invent a different structure.

JSP views live under `src/main/webapp/WEB-INF/views/<feature>/`, matching the same split.

## Module status

All six modules (`user`, `catalog`, `cart`, `order`, `feedback`, `support`) are implemented and smoke-tested end-to-end against a real running app and MySQL instance — not just code review. `user` is the original reference module; read it first if you want to see the intended pattern (`entity/repository/service/controller/dto`, DTOs never bound directly to entities, ownership checks via `CurrentUserProvider`, CSRF-protected forms, `fieldErrors`/`errorMessage` flash pattern) before touching another module.

A few cross-module design notes worth knowing before you change things:
- **Order status is per-item, not per-order.** An `Order` can contain items from multiple merchants, so each merchant only ever advances their own `OrderItem.status`; `Order.status` is a computed rollup (see `OrderService.recomputeOrderStatus()`), never set directly.
- **Checkout deducts stock** and blocks with a `BusinessRuleViolationException` if any cart item exceeds available stock.
- **Product flagging is recomputed on every review**, in both directions — a product can un-flag itself if its average recovers, not just flag once and stay flagged.
- **Catalog's product detail page renders review data** (feedback module) and **order detail links out to feedback's review form** — the one place where "later" modules reach back into earlier ones' views, by design.

## Adding a new protected URL

All authentication/authorization lives in **one file**: `config/SecurityConfig.java`. You should not need to touch its `PasswordEncoder` bean, its login/logout setup, or its session settings — those are shared and already working. The only thing you'll ever add is one line to the `authorizeHttpRequests` block, matching your module's URL prefix to the role that should be allowed:

```java
.requestMatchers("/your-prefix/**").hasRole("CUSTOMER")   // or MERCHANT / PLATFORM_EMPLOYEE
```

Existing prefixes already covered: `/customer/**`, `/cart/**`, `/checkout/**`, `/orders/**`, `/reviews/submit/**`, `/inquiries/**` → `CUSTOMER`; `/merchant/**` → `MERCHANT`; `/employee/**` → `PLATFORM_EMPLOYEE`. If your module's URLs fall under one of these prefixes already, you don't need to change `SecurityConfig` at all.

To get the currently logged-in user in a controller, add a parameter: `@AuthenticationPrincipal AppUserPrincipal principal` — `principal.getId()` / `principal.getUser()` gives you the real entity. For service-layer code (not a controller), inject `CurrentUserProvider` instead and call `getCurrentUserId()`.

**Ownership rule:** never accept an id that identifies "which record" from client input (a URL path variable, a hidden form field) when the record should belong to the current user. Resolve it from `CurrentUserProvider`/`@AuthenticationPrincipal` instead — see `MerchantProfileService` for the pattern. If a record can legitimately belong to someone else (e.g. an employee approving a merchant by id), accepting the id is fine, but the *permission* to act on it must still come from the role check in `SecurityConfig`, not from trusting the request.

## Tests

`./mvnw test` runs the test suite. See `user/controller/EmployeeMerchantControllerTest` and `user/service/UserRegistrationServiceTest` for the patterns to follow: `@WebMvcTest` + `@WithMockUser` for controllers (fast, no real database), plain Mockito unit tests for services.
