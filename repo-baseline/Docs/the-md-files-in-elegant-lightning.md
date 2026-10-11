# Web-Based Multi-Merchant Shopping Mall System — Build Plan

## Context

This is a brand-new SE2030 (SLIIT) group project — no code exists yet, only three planning docs in `Docs/` (project overview, module spec, proposal report). The docs describe the six required functions, three stakeholder roles (Customer, Merchant, Platform Employee), and a 4-sprint Agile timeline (Wk3–12) with one function owned per team member.

The docs specify Microsoft SQL Server, but the team is not locked into that choice — MySQL was chosen instead as the easiest database to set up and run locally (lighter install, no licensing friction, works the same way through Spring Data JPA). This is a deliberate, confirmed deviation from the written docs; the docs should be updated to say MySQL when convenient, but that's a documentation follow-up, not a blocker for build.

Goal of this plan: stand up a shared Spring Boot + JSP + MySQL project skeleton the whole 6-person team can build on, then fully implement **User & Stakeholder Management** end-to-end as the reference module (it's the foundation — auth/RBAC — everything else depends on it), plus a concrete implementation outline for each of the other five modules so their owners can follow the same pattern.

## Tech stack

Java 17, Spring Boot 3.x, JSP + JSTL (presentation), Spring Data JPA/Hibernate, MySQL (`mysql-connector-j`), Maven, Spring Security, Bean Validation, Lombok (optional, reduces boilerplate).

## Part A — Shared project skeleton (build first, before per-module work)

### A.1 Maven project
- Single Maven project, packaging `war` (needed for `tomcat-embed-jasper` to compile JSPs), runnable via `mvn spring-boot:run` with embedded Tomcat — no external app server needed for dev/demo.
- `pom.xml` dependencies: `spring-boot-starter-web`, `tomcat-embed-jasper`, `jstl` (`org.glassfish.web:jakarta.servlet.jsp.jstl`), `spring-boot-starter-data-jpa`, `mysql-connector-j`, `spring-boot-starter-security`, `spring-boot-starter-validation`, `lombok`, `spring-boot-starter-test` + `spring-security-test`.
- `application.properties`: MySQL connection URL/user/password, `spring.jpa.hibernate.ddl-auto=update`, `spring.mvc.view.prefix=/WEB-INF/views/`, `spring.mvc.view.suffix=.jsp`, `server.servlet.session.timeout=30m`. JSPs must live under `src/main/webapp/WEB-INF/views/` (Jasper compiles from the webapp root, not the classpath).

### A.2 Package structure — package-by-feature

Each of the 6 members owns one feature package end-to-end, all following the same internal shape (`entity/repository/service/controller/dto`) — this consistency is what makes the reference module (Part B) copyable by the rest of the team.

```
com.sliit.se2030.mall
├── MallApplication.java
├── common/
│   ├── entity/BaseEntity.java          (@MappedSuperclass: id, createdAt, updatedAt)
│   ├── exception/                      (ResourceNotFoundException, AccessDeniedForResourceException,
│   │                                     BusinessRuleViolationException, GlobalExceptionHandler)
│   ├── dto/                            (shared pagination/response wrappers)
│   └── util/CurrentUserProvider.java   (pulls logged-in user/role from SecurityContext)
├── config/
│   ├── SecurityConfig.java             (SecurityFilterChain, RBAC URL rules, BCrypt bean, form login)
│   ├── WebMvcConfig.java
│   └── DataSeedConfig.java             (seeds one hardcoded PlatformEmployee account)
├── security/
│   ├── AppUserPrincipal.java           (implements UserDetails)
│   └── AppUserDetailsService.java      (implements UserDetailsService)
├── user/       (Module 1 — User & Stakeholder Management — build first, reference module)
├── catalog/    (Module 2 — Product Catalog Management)
├── cart/       (Module 3 — Shopping Cart Handling)
├── order/      (Module 4 — Order and Payment Handling)
├── feedback/   (Module 5 — Product Feedback & Reputation Analytics)
└── support/    (Module 6 — Customer Support and Inquiry System)
```

`src/main/webapp/WEB-INF/views/` mirrors the same split (`common/`, `auth/`, `user/`, `catalog/`, `cart/`, `order/`, `feedback/`, `support/`).

### A.3 Cross-cutting pieces to build once, up front
1. **`BaseEntity`** — `id`, `createdAt`, `updatedAt`; every module's entities extend it.
2. **`SecurityConfig`** — one shared `SecurityFilterChain` covering the whole app (detailed in B.5); other members only ever add one `requestMatchers("/their-prefix/**").hasRole(...)` line for their own module.
3. **`GlobalExceptionHandler`** (`@ControllerAdvice`) — maps `ResourceNotFoundException`→404, access-denied→403, `BusinessRuleViolationException`→redirect with flash error, so no module reinvents error handling.
4. **DTO convention** — JSP forms bind to `*Form`/`*Request` DTOs, never directly to entities (avoids over-posting, keeps JPA entities decoupled from HTML forms).
5. **`CurrentUserProvider`** — centralizes "get current logged-in Merchant/Customer id," used by every module to enforce merchants only ever touching their own data (the NFR from the spec).

### A.4 Entity model and cross-module relationships

**User hierarchy** (module `user`): `User` (abstract, `@Entity`, `@Inheritance(strategy = SINGLE_TABLE)`, `@DiscriminatorColumn("user_type")`) with fields `email` (unique), `passwordHash`, `fullName`, `phone`, `enabled`, `role` (enum `CUSTOMER`/`MERCHANT`/`PLATFORM_EMPLOYEE`, doubles as the Spring Security authority). Subclasses: `Customer` (shipping address), `Merchant` (`shopName`, `shopDescription`, `verificationStatus` enum `PENDING`/`APPROVED`/`SUSPENDED`/`REJECTED`, `verifiedAt`, `verifiedByEmployeeId`), `PlatformEmployee` (`employeeCode`). Single-table inheritance keeps login (`findByEmail`) a one-table lookup.

**Catalog** (module `catalog`): `Category` (name, optional self-referencing parent), `Product` (name, description, price, stockQuantity, imageUrl, active, `flaggedForReview` boolean, FK `Merchant`, FK `Category`).

**Cart** (module `cart`): `Cart` (1:1 FK `Customer` — tied to the customer record, not the HTTP session, so it persists across sessions), `CartItem` (FK `Cart`, FK `Product`, `quantity`, unique `(cart_id, product_id)`). Totals are computed in the service layer (`quantity * product.price`), never stored, to avoid staleness when price changes.

**Order & Payment** (module `order`): `Order` (FK `Customer`, `status` enum `PENDING`/`CONFIRMED`/`SHIPPED`/`DELIVERED`/`CANCELLED`, `totalAmount` snapshot, `shippingAddress` snapshot copy), `OrderItem` (FK `Order`, FK `Product`, denormalized `merchantId`, `quantitySnapshot`, `unitPriceSnapshot` — never read live product price for historical orders), `Payment` (1:1 FK `Order`, `status` enum `SIMULATED_SUCCESS`/`SIMULATED_FAILED`, `transactionRef` UUID — mocked, no real gateway). `OrderService.checkout()` calls `PaymentService.simulatePayment()` in one transaction, satisfying the "Checkout includes Make Payment" relationship.

**Feedback** (module `feedback`): `Review` (FK `Customer`, FK `Product`, `rating` 1–5, `comment`, FK `OrderItem verifiedOrderItem` **not null** — the enforcement mechanism for verified-purchase-only reviews: service checks `orderItem.order.customer == currentCustomer && orderItem.order.status == DELIVERED`; unique `(customer_id, order_item_id)` blocks duplicates). `MerchantResponse` (1:1 FK `Review`, FK `Merchant respondedBy`, must equal `review.product.merchant`). Rating distribution and merchant reputation score are computed via aggregate queries (`AVG`/`GROUP BY`), not stored, except an optional denormalized `Merchant.reputationScore` cache recalculated on each new review. Low-rating flag: after saving a review, recompute the product's average; below a configured threshold (e.g. 2.5) sets `Product.flaggedForReview = true`.

**Support** (module `support`): `Inquiry` (FK `Customer`, nullable FK `Order`, nullable FK `Product` — must reference at least one, checked in the service layer, `status` enum `OPEN`/`IN_PROGRESS`/`RESOLVED`, nullable FK `PlatformEmployee assignedEmployee`), `InquiryResponse` (FK `Inquiry`, FK `User respondedBy` — employee or merchant), `FAQ` (standalone: question, answer, category) — lowest priority within this module, marked optional in the spec.

**Key cross-module coupling to flag to the whole team:** `OrderItem` (module 4) is referenced by `Review` (module 5) as the verified-purchase proof — this is the one FK relationship that ties two different members' modules together directly, so `order` should land before `feedback` needs it for integration testing.

### A.5 Build sequence (maps onto the existing Wk3–12 sprint plan)
1. **Wk3–5 (Sprint 1):** User & Stakeholder Management owner builds `common/`, `config/SecurityConfig.java`, `security/`, and the full `user` module (Part B). Everyone else scaffolds their own empty package skeleton on a shared branch in parallel.
2. Once login + `SecurityConfig` work, the other 5 members build their modules in parallel against `CurrentUserProvider`/`Authentication` for role checks.
3. Sequencing to watch: `catalog` should land before `cart`/`order` need `Product` as a FK target; `order`/`OrderItem` must land before `feedback` can enforce verified-purchase — flagged for Sprint 3 ("Integration," Wk9) if a module runs behind.

## Part B — Reference module: User & Stakeholder Management (full end-to-end)

### B.1 Entities — see A.4 User hierarchy. `Role` enum (`CUSTOMER`, `MERCHANT`, `PLATFORM_EMPLOYEE`) doubles as the Spring Security authority (`ROLE_CUSTOMER`, etc.). `VerificationStatus` enum (`PENDING`, `APPROVED`, `SUSPENDED`, `REJECTED`).

### B.2 Repositories
`UserRepository` (`findByEmail`, `existsByEmail` — used by login), `MerchantRepository` (`findByVerificationStatus` for the employee's pending-approvals list), `CustomerRepository`, `PlatformEmployeeRepository` — mostly inherited CRUD.

### B.3 Services
- `UserRegistrationService` — `registerCustomer`/`registerMerchant`: validate email uniqueness, hash password with `PasswordEncoder`, set role; merchant starts `verificationStatus = PENDING` (allow login pre-approval but restrict to a "pending approval" landing page via a controller-level check, not a hard security block).
- `MerchantVerificationService` — `listPending()`, `approveMerchant`, `suspendMerchant` (also sets `User.enabled = false` so Spring Security blocks login outright), `rejectMerchant`.
- `MerchantProfileService` — `updateShopProfile`, must verify `merchantId == currentUser.id` via `CurrentUserProvider` (never trust a path variable alone).
- `AppUserDetailsService` (in `security/`) — `loadUserByUsername(email)` → `UserRepository.findByEmail` → wraps in `AppUserPrincipal`, maps `role` to `SimpleGrantedAuthority`, maps `enabled`.

### B.4 Controllers + JSP views
- `AuthController` — `/login` (GET), `/register/customer`, `/register/merchant` (GET+POST). Actual auth POST handled by Spring Security's form-login filter.
- `MerchantProfileController` — `/merchant/profile` (GET+POST), restricted to `ROLE_MERCHANT`.
- `EmployeeMerchantController` — `/employee/merchants/pending`, `/employee/merchants/{id}/approve|suspend|reject`, restricted to `ROLE_PLATFORM_EMPLOYEE`.
- `HomeController` — `/customer/home`, `/merchant/dashboard`, `/employee/dashboard` — post-login redirect targets, later extended by other modules with their own widgets.

JSPs: `common/` (layout-header, layout-footer, navbar with role-aware `<sec:authorize>`, access-denied, error), `auth/login.jsp` (Spring Security form-login markup, CSRF hidden field), `auth/register-customer.jsp`, `auth/register-merchant.jsp`, `user/merchant-profile.jsp`, `user/employee-merchant-list.jsp`, plus the three dashboard placeholders.

### B.5 Spring Security config (`config/SecurityConfig.java`)
- Spring Boot 3.x style: single `@Bean SecurityFilterChain` (no `WebSecurityConfigurerAdapter`).
- `BCryptPasswordEncoder` bean, wired into a `DaoAuthenticationProvider` with `AppUserDetailsService`.
- URL rules, most-specific first: `permitAll()` on `/`, `/login`, `/register/**`, static assets, `/catalog/**` (public browsing); `hasRole("CUSTOMER")` on `/customer/**`, `/cart/**`, `/checkout/**`, `/orders/**`, `/reviews/submit/**`, `/inquiries/**`; `hasRole("MERCHANT")` on `/merchant/**`; `hasRole("PLATFORM_EMPLOYEE")` on `/employee/**`; `anyRequest().authenticated()` catch-all.
- Form login with a custom `AuthenticationSuccessHandler` for role-based redirect (`/customer/home` vs `/merchant/dashboard` vs `/employee/dashboard`).
- Logout: invalidate session, delete `JSESSIONID`.
- CSRF stays enabled (Spring Security default) — every JSP `<form>` POST needs the CSRF hidden token; flag this to the team as a common pitfall (forms silently 403ing).
- Session: `sessionFixation().migrateSession()`, `maximumSessions(1)`, `30m` timeout — satisfies the "secure session management" NFR.

### B.6 Implementation order
1. Shared pieces from Part A (`BaseEntity`, `GlobalExceptionHandler`, DB connection) — confirm `mvn spring-boot:run` boots against an empty DB.
2. `Role`/`VerificationStatus` enums → `User` (with inheritance) → `Customer`/`Merchant`/`PlatformEmployee` — confirm Hibernate DDL creates one `users` table with a `user_type` discriminator.
3. Repositories.
4. `AppUserPrincipal` + `AppUserDetailsService`.
5. `SecurityConfig` — confirm unauthenticated requests redirect to `/login`.
6. Seed one hardcoded `PlatformEmployee` (`CommandLineRunner` or `data.sql` with a pre-hashed BCrypt password) — unblocks manual testing of merchant approval before the registration UI exists.
7. `UserRegistrationService` + `AuthController` + registration JSPs — test: register a customer, log in, confirm role-based redirect and `/merchant/**` blocked.
8. `MerchantVerificationService` + `EmployeeMerchantController` + JSP — test: register a merchant (`PENDING`), approve as seeded employee, confirm status flips.
9. `MerchantProfileService` + controller + JSP — verify a merchant can't edit another merchant's profile by tampering with the URL id (service-layer check, not just UI hiding).
10. Role dashboard placeholders + `AuthenticationSuccessHandler`.
11. Write a short "how to add a protected URL" note for the other 5 members.
12. Basic tests: one `@WebMvcTest` per controller confirming role-gated 403s; one service test confirming BCrypt hashing and duplicate-email rejection.

## Part C — Implementation outline for the other five modules

Each owner follows the same `entity/repository/service/controller/dto` shape as Module 1, against the entities already defined in A.4.

**Module 2 — Product Catalog Management** (`catalog`)
- Entities/repos: `Product`, `Category` — repository finder methods for name search (`findByNameContainingIgnoreCase`), filter by category/price range/merchant (Spring Data JPA `Specification` or derived query methods).
- Services: `ProductService` (CRUD, scoped to `currentUser.merchant` for merchant-side create/edit — enforced via `CurrentUserProvider`), `CategoryService`.
- Controllers/JSP: public `/catalog` (browse, search, filter, product detail page — `permitAll`), merchant-only `/merchant/products/**` (CRUD, restricted to `ROLE_MERCHANT`, each product auto-tagged with `currentUser.merchant`).
- Depends on Module 1 (`Merchant`) existing; feeds Modules 3/4 (`Product` FK).

**Module 3 — Shopping Cart Handling** (`cart`)
- Entities/repos: `Cart`, `CartItem`.
- Services: `CartService` — `addItem`, `updateQuantity`, `removeItem`, `getCartWithTotal` (computes running total in-service, not stored).
- Controllers/JSP: `/cart/**`, `ROLE_CUSTOMER` only — add-to-cart action from the catalog product page, cart view page with quantity update/remove.
- Depends on Modules 1 (`Customer`) and 2 (`Product`).

**Module 4 — Order and Payment Handling** (`order`)
- Entities/repos: `Order`, `OrderItem`, `Payment`.
- Services: `OrderService.checkout(cart)` (converts cart → order, snapshots price/address, clears cart, calls `PaymentService.simulatePayment` in one transaction), `OrderTrackingService` (status updates), merchant-side `MerchantOrderService` (view/accept/update status for their own `OrderItem`s only, via denormalized `merchantId`).
- Controllers/JSP: `/checkout/**`, `/orders/**` (`ROLE_CUSTOMER` — checkout flow, confirmation page, order history/tracking), `/merchant/orders/**` (`ROLE_MERCHANT` — order management).
- Depends on Modules 1 and 3; feeds Module 5 (`OrderItem` as verified-purchase proof).

**Module 5 — Product Feedback & Reputation Analytics** (`feedback`)
- Entities/repos: `Review`, `MerchantResponse`.
- Services: `ReviewService` (create review — validates verified purchase via `OrderItem`, recomputes product average, applies low-rating flag threshold), `ReputationService` (aggregate merchant reputation score, rating distribution per product), `MerchantResponseService`.
- Controllers/JSP: `/reviews/submit/**` (`ROLE_CUSTOMER`, from order history — "leave a review" only appears for delivered `OrderItem`s without an existing review), product detail page review list (public), `/merchant/feedback/**` (`ROLE_MERCHANT` — dashboard: rating trends, top/lowest-rated products, respond to reviews), `/employee/flagged-products` (`ROLE_PLATFORM_EMPLOYEE`).
- Depends on Modules 1, 2, and 4 (`OrderItem`) — build last among the customer-facing modules, or stub the verified-purchase check against dummy data early and wire the real check once Module 4 lands.

**Module 6 — Customer Support and Inquiry System** (`support`)
- Entities/repos: `Inquiry`, `InquiryResponse`, `FAQ`.
- Services: `InquiryService` (submit, list by customer, assign/respond, status transitions), `FaqService` (simple CRUD, lowest priority).
- Controllers/JSP: `/inquiries/**` (`ROLE_CUSTOMER` — submit tied to an order/product, view own status), `/employee/inquiries/**` (`ROLE_PLATFORM_EMPLOYEE` — respond/resolve, optionally loop in the merchant), public `/faq`.
- Depends on Module 1; loosely depends on Modules 2/4 for the optional order/product linkage (nullable FKs, so this module isn't blocked if those land late).

## Verification

- `mvn spring-boot:run` boots cleanly against a local MySQL instance with an empty schema; Hibernate DDL auto-creates all tables including the single-table `users` inheritance with `user_type` discriminator.
- Manual smoke test of the reference module: register a customer → log in → redirected to `/customer/home`; register a merchant → status `PENDING` → log in as seeded `PlatformEmployee` → approve → merchant's status flips to `APPROVED`; confirm `/merchant/**` and `/employee/**` are 403 for a logged-in customer, and vice versa.
- Confirm CSRF-protected forms (login, registration, approve/suspend) submit successfully with the hidden token present.
- As each of the other 5 modules lands, smoke-test its own role-gated URLs the same way, then run one end-to-end path once Modules 2–5 exist together: browse catalog → add to cart → checkout (simulated payment) → order appears in history → leave a verified-purchase review → merchant sees it on their feedback dashboard.
- `mvn test` passes for the `@WebMvcTest`/service-layer tests written in Part B step 12, as a baseline other members extend for their own modules.
