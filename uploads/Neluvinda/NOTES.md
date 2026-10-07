# Neluvinda T.M.D.N. — User & Stakeholder Management

## 1. What this module does (30-second version)
Centralized identity and access control for the whole platform. It handles customer
and merchant self-registration, a platform employee approving/suspending merchants,
and a merchant editing their own shop profile. Every other module trusts this one to
answer "who is this person and are they allowed to be here."

Persona tie-in: this covers the "New merchant" persona from the sprint plan —
registers, gets verified by an employee, then can start selling.

## 2. File-by-file walkthrough

### Entities (`user/entity/`)
- **`User.java`** — abstract base class for every actor. Uses JPA
  `SINGLE_TABLE` inheritance: `Customer`, `Merchant`, and `PlatformEmployee` all live
  in one `users` table, distinguished by a `user_type` discriminator column. This is
  why login can look up "any user by email" with one query regardless of role —
  there's no need to check three separate tables.
- **`Customer.java`** — adds `shippingAddress`. Nothing else; deliberately thin.
- **`Merchant.java`** — adds `shopName`, `shopDescription`, `verificationStatus`
  (defaults to `PENDING`), `verifiedAt`, and `verifiedByEmployeeId`. Note
  `verifiedByEmployeeId` is a plain `Long`, not a `@ManyToOne` to `PlatformEmployee` —
  it's there only for an audit trail ("who approved this"), not for navigating back to
  the employee object, so a real foreign key would be overkill.
- **`PlatformEmployee.java`** — adds `employeeCode`. Also thin.
- **`VerificationStatus.java`** — enum: `PENDING`, `APPROVED`, `SUSPENDED`, `REJECTED`.

### Services (`user/service/`)
- **`UserRegistrationService.java`** — `registerCustomer()` and `registerMerchant()`.
  Both check `assertEmailAvailable()` first (throws `BusinessRuleViolationException` if
  taken) and hash the password with the injected `PasswordEncoder` before saving —
  never store plaintext. A newly registered merchant is `PENDING` by default (from the
  `Merchant` field initializer) and can log in immediately, but stays restricted until
  approved — see below.
- **`MerchantVerificationService.java`** — `listPending()`, `approveMerchant()`,
  `suspendMerchant()`, `rejectMerchant()`. `suspendMerchant()` also sets
  `enabled=false`, which blocks login entirely at the Spring Security level (a
  suspended merchant can't even see a "wrong password" screen, they're refused
  outright). All three actions are `@Transactional` and record `employeeId` for audit.
- **`MerchantProfileService.java`** — lets a merchant view/update their *own* shop
  profile. Notice there's no `id` parameter anywhere in this class — "which merchant"
  always comes from `CurrentUserProvider.getCurrentUserId()` (the authenticated
  session), never from client input. This is the ownership pattern you'll see repeated
  in every other module (cart, catalog, orders, feedback).

### Controllers (`user/controller/`)
- **`AuthController.java`** — serves the login page (`GET /login`) and both
  registration forms/submissions. Important: there's **no** `@PostMapping("/login")`
  here — Spring Security's own form-login filter intercepts `POST /login` before this
  controller ever sees it (configured in `SecurityConfig`, which is Ariyasinghe's
  file). This controller only serves the login *page*.
- **`EmployeeMerchantController.java`** — `/employee/merchants/pending` (list),
  `/{id}/approve`, `/{id}/suspend`, `/{id}/reject`. All POST actions pull the acting
  employee's id from `@AuthenticationPrincipal AppUserPrincipal`, not a form field.
- **`MerchantProfileController.java`** — `/merchant/profile` GET/POST.

### Tests
- **`UserRegistrationServiceTest.java`** — covers the registration service.
- **`EmployeeMerchantControllerTest.java`** — covers the employee-facing controller.
Run them with `mvn test`. Read through both before viva — you should be able to
explain what each test asserts and why, not just that they pass.

## 3. Design decisions likely to come up
- **Why single-table inheritance instead of three separate tables?** One `email`
  lookup at login time works for any role without a join or a "check three tables"
  loop. Trade-off: nullable columns for role-specific fields (e.g. `shopName` is
  `NULL` for a customer row) — acceptable at this project's scale.
- **Why does a merchant get to log in before being approved?** So they can complete
  their profile and see their pending status, but they're blocked from doing anything
  merchant-specific until `APPROVED` — that restriction is enforced in the *other*
  modules' services (e.g. `ProductService`), not here. If asked "where is that
  enforced," be honest that it's currently just "merchant can log in" +
  role-based URL restriction in `SecurityConfig` — there's no explicit
  `verificationStatus == APPROVED` gate in `ProductService` yet. That's a legitimate
  gap to mention if asked, or a good "try this yourself" candidate (see below).
- **Why `BusinessRuleViolationException` for duplicate email** rather than a DB
  unique-constraint violation bubbling up? Gives a clean, user-facing error message
  instead of a raw SQL exception leaking to the view.

## 4. How this connects to other modules
- **Everything depends on `User`/`Customer`/`Merchant`** — catalog's `Product` has a
  `Merchant`, cart/orders have a `Customer`, feedback ties reviews to both.
- **`CurrentUserProvider`** (shared infra, in Ariyasinghe's `common/` folder) reads the
  logged-in user's id from Spring Security's context — every module's "ownership
  check" pattern depends on this.
- Your controllers' URL prefixes (`/employee/**`, `/merchant/**`) are locked down in
  `SecurityConfig` (Ariyasinghe's file) — you don't add security annotations yourself.

## 5. Likely viva questions
- **"Why is `Merchant.shopName` not `nullable = false` in the database if it's
  required for merchants?"** — Because the column is shared across all three roles in
  one table; a customer row legitimately has `shopName = NULL`. The requirement is
  enforced by the constructor and Bean Validation on the registration form instead of
  a DB constraint.
- **"What happens if two people register with the same email at the exact same
  time?"** — Honest answer: there's a race condition. `assertEmailAvailable` checks
  then the save happens in a separate step; without a DB-level unique constraint check
  at save time (there is one on the `email` column — `@Column(unique = true)` — so the
  second save would actually fail with a constraint violation that isn't currently
  caught as a friendly `BusinessRuleViolationException`). Worth knowing this exists
  rather than pretending it doesn't.
- **"How does suspending a merchant actually stop them from doing anything?"** —
  `enabled=false` is read by Spring Security's user details layer at login time; a
  disabled account can't authenticate at all, not just "can't access merchant pages."
- **"Why is `verifiedByEmployeeId` a plain Long instead of a relationship?"** — Audit
  trail only; we never need to navigate from a merchant back to the approving
  employee's full object, so a real `@ManyToOne` would add an unnecessary join.

## 6. Try-this-yourself (to have something you genuinely wrote)
- Add a check in `ProductService.createProduct()` (Bandara's file, but worth
  discussing together) that throws if the merchant's `verificationStatus` isn't
  `APPROVED` — closes the gap mentioned above. Small, real, and testable.
- Write one more unit test for `MerchantVerificationService` — e.g. that
  `suspendMerchant()` actually sets `enabled=false`.
- Add a "reason" field to rejection (`rejectMerchant`) so an employee can leave a note
  — touches entity, service, DTO, and controller, small enough to do in an hour, and
  gives you a real end-to-end change to describe at viva.
