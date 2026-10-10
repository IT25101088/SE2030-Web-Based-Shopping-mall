# Ariyasinghe P.A.D.A.J. — Customer Support & Inquiry + Frontend + Config

Your own scope is the widest (your CRUD, every JSP, and shared config/security), so
this note is split into two parts: your actual CRUD (needs full viva depth) and the
frontend/config work (needs enough depth to explain the choices, even though it spans
every module).

## Part A — Customer Support and Inquiry System (your core function)

### 1. What it does
Lets a customer raise an inquiry tied to an order or a product, track its status, and
get responses from a platform employee (or merchant); includes a public FAQ page.
Persona: the "Support agent" (Platform Employee) and the customer raising the inquiry.

### 2. File-by-file walkthrough
- **`Inquiry.java`** — `relatedOrder` and `relatedProduct` are both **nullable at the
  DB level** — the rule "must reference at least one of them" is enforced in
  `InquiryService`, not as a database constraint. Deliberately kept simple rather than
  a DB-level `CHECK` constraint, per the project's "simpler over sophisticated"
  approach. `assignedEmployee` starts `null` and gets set the first time an employee
  responds.
- **`InquiryResponse.java`** — `respondedBy` is typed as the abstract `User`, not
  `Customer`/`Merchant`/`PlatformEmployee` specifically, because either an employee or
  a merchant might respond. This works because of the single-table inheritance
  Neluvinda's module set up — one `users` table underlies all three subtypes, so a
  `User` reference resolves fine regardless of which concrete role responded.
- **`InquiryStatus.java`** — `OPEN`, `IN_PROGRESS`, `RESOLVED`.
- **`InquiryService.submitInquiry()`** — throws `BusinessRuleViolationException` if
  *both* `relatedOrderId` and `relatedProductId` are null; if an order is given, it
  checks the order belongs to the submitting customer (ownership check, same pattern
  as every other module).
- **`InquiryService.respondToInquiry()`** — saves the response, and has two pieces of
  "implicit" logic worth being able to explain: (1) if the inquiry was `OPEN`, it
  auto-advances to `IN_PROGRESS` on the first response; (2) if no employee is assigned
  yet and the responder is a `PlatformEmployee` (checked via `instanceof` pattern
  matching), they become the `assignedEmployee`. A merchant can also respond without
  being assigned — worth noting that distinction if asked.
- **`InquiryService.updateStatus()`** — used by the "resolve" action; no restriction
  currently on who can move status backward from `RESOLVED` — an honest gap if asked.
- **`FaqService`** — deliberately the simplest file in the whole project: read-only
  `listAll()`. The code comment even says "lowest priority within this module per the
  spec" — it's the optional FAQ mentioned in the backlog, not a core deliverable.

### 3. Likely viva questions
- **"Why not a DB constraint for 'must have an order or a product'?"** — Consistent
  with the project's philosophy of keeping the schema simple and putting business
  rules in the service layer where they're easier to read, test, and change.
- **"Who can resolve an inquiry?"** — Any platform employee, via
  `EmployeeInquiryController.resolve()`; there's no check that it's specifically the
  *assigned* employee — anyone with the `PLATFORM_EMPLOYEE` role can resolve any
  inquiry. Worth naming as a design choice (small platform-employee team, blanket
  trust) or a possible improvement.
- **"Can a merchant see inquiries about their own products?"** — Currently no —
  `EmployeeInquiryController` is the only inquiry-listing controller, restricted to
  `PLATFORM_EMPLOYEE`. A merchant-facing inquiry view isn't implemented. Honest gap.

### Try-this-yourself
- Add a check in `updateStatus()` restricting who can move a `RESOLVED` inquiry back
  to `OPEN` (e.g. only the assigned employee).
- Add a unit test for `submitInquiry()` covering the "neither order nor product"
  rejection path.

---

## Part B — Frontend (all JSPs) + Configuration

You didn't design each screen's business logic (that's each module owner's), but you
own how it's rendered and wired together. Focus your explanation on the *shared*
patterns repeated across every JSP, not memorizing all 27 files individually.

### Shared JSP patterns worth knowing cold
- **`common/layout-header.jsp` / `layout-footer.jsp`** — included at the top/bottom of
  every page for a consistent nav bar; uses the `spring-security-taglibs`
  dependency's `<sec:authorize>` tag to show/hide links based on role (e.g. "Merchant
  Dashboard" link only shows for `ROLE_MERCHANT`) without any controller needing to
  pass that down manually.
- **View resolution** — controllers return plain strings like `"auth/login"`;
  `application.properties` (`spring.mvc.view.prefix`/`suffix`) maps that to
  `/WEB-INF/views/auth/login.jsp`. `/WEB-INF/**` is deliberately never reachable
  directly by a browser (servlet containers block it) — that's why it's safe to
  `permitAll()` in `SecurityConfig` alongside the genuinely public pages.
- **Form binding pattern** — nearly every form-handling controller follows the same
  shape: `@Valid @ModelAttribute("form") SomeForm form, BindingResult bindingResult` →
  if errors, re-render the same JSP with `fieldErrors` in the model; else call the
  service and redirect. Once you can explain this pattern once, you can explain why
  every `*-form.jsp` looks the way it does.
- **`common/error.jsp` / `access-denied.jsp`** — the generic fallback views for
  unhandled exceptions (via `GlobalExceptionHandler`) and 403s (via Spring Security).

### Configuration
- **`SecurityConfig.java`** — the single shared authorization map: which URL prefixes
  need which role (`/customer/**` → `CUSTOMER`, `/merchant/**` → `MERCHANT`,
  `/employee/**` → `PLATFORM_EMPLOYEE`, rest → just authenticated). Each module owner
  added their own prefix here as they built their controllers, but you own the file
  as a whole — be ready to explain the full role map, not just your own prefixes.
  Also configures form login (`loginProcessingUrl("/login")` — note `AuthController`
  only serves the login *page*, this filter handles the actual POST), logout, and
  `maximumSessions(1)` (a second login invalidates the first session).
- **`DataSeedConfig.java`** — seeds one hardcoded platform employee
  (`admin@mall.local` / `admin123`) on startup so there's always a way to log in and
  approve merchants before any registration UI existed. The code comment explicitly
  ties this to the project docs' stated assumption: "single/small number of hardcoded
  platform employee accounts for demo purposes" — not an invented shortcut.
- **`AppUserDetailsService.java` / `AppUserPrincipal.java` /
  `RoleBasedAuthenticationSuccessHandler.java`** — the glue between a `User` entity and
  Spring Security's login mechanism; the success handler is what redirects a customer
  to `/customer/home`, a merchant to `/merchant/dashboard`, etc. based on role after
  login.
- **`common/exception/GlobalExceptionHandler.java`** — catches the custom exceptions
  used everywhere (`ResourceNotFoundException`, `AccessDeniedForResourceException`,
  `BusinessRuleViolationException`) and maps them to appropriate views/status codes,
  so no individual controller needs its own try/catch for these.

### Likely viva questions (frontend/config)
- **"Why `<sec:authorize>` in JSPs instead of each controller deciding what to show?"**
  — Keeps the nav bar's role-visibility logic in one shared file instead of
  duplicated across every controller that renders a page with navigation.
- **"Why is `/WEB-INF/**` in the `permitAll()` list — isn't that a security hole?"** —
  No: the servlet container itself refuses any direct browser request to `/WEB-INF/**`
  regardless of what Spring Security says. It's listed there because *rendering* any
  view (even a public one) is internally an HTTP forward to that path, which Spring
  Security would otherwise re-check and potentially block.
- **"What happens if two people log in as the same seeded admin account at once?"** —
  `maximumSessions(1)` means the second login invalidates the first session; only one
  active session per account.

### Try-this-yourself
- Add a shared client-side validation hint (e.g. required-field styling) to one form
  JSP and be ready to explain why you didn't rely on it alone (server-side `@Valid`
  is still the real enforcement — client-side is just UX).
- Trace one full request end-to-end in the debugger (e.g. `POST /register/customer`)
  from `SecurityConfig`'s permit rule, through `AuthController`, to the JSP re-render
  on validation failure — this is the single best way to genuinely internalize how
  the pieces fit together rather than just reading the notes.
