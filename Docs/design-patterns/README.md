# Design Patterns in the Serendib Central Shopping Mall — Viva Guide

This folder prepares each team member to **demonstrate and explain** the design
pattern used in their module, as required for the Week 13 final presentation
(design pattern component = **5 marks**, awarded on the work shown *and* your
understanding).

Everything here is based on the two SE2030 lectures:

- **Lecture 8 – Design Patterns Part I** (`Docs/Week11_Lecture_DesignPattern-Part01.pdf`) — Singleton, Observer
- **Design Patterns Part II** (`Docs/DesignPattern PartII.pdf`) — Strategy, Factory, Decorator

---

## 1. One-page summary

| # | Module (major function) | Pattern(s) | Category | Package | Guide |
|---|---|---|---|---|---|
| 1 | Customer Support & Inquiries | **Observer** | Behavioral | `support/observer` | [01-support-observer.md](01-support-observer.md) |
| 2 | Order & Payment Handling | **Strategy** + **Factory** + **Singleton** | Behavioral + Creational | `order/payment` | [02-order-payment-strategy-factory-singleton.md](02-order-payment-strategy-factory-singleton.md) |
| 3 | Product Catalogue | **Strategy** (sorting) | Behavioral | `catalog/sort` | [03-catalog-sort-strategy.md](03-catalog-sort-strategy.md) |
| 4 | Shopping Cart | **Decorator** | Structural | `cart/pricing` | [04-cart-decorator.md](04-cart-decorator.md) |
| 5 | User & Stakeholder Management | **Factory** | Creational | `user/factory` | [05-user-factory.md](05-user-factory.md) |
| 6 | Product Feedback & Reputation | **Strategy** (rating rule) | Behavioral | `feedback/rating` | [06-feedback-rating-strategy.md](06-feedback-rating-strategy.md) |

All packages are under `src/main/java/com/sliit/se2030/mall/`.

Together the system uses **all five patterns taught in the lectures**, and
covers all three GoF categories.

---

## 2. What the lecturer expects you to know (from the slides)

### What is a design pattern? (Part I, slides 3–5)

> "A proven solution to a common software design problem in a specific context."

- A **reusable template** for a recurring design problem — not finished code.
- Gives developers a **shared vocabulary** ("we used a Strategy here").
- Idea from **Christopher Alexander** (architect, 1977); formalised for software
  by the **Gang of Four** (Gamma, Helm, Johnson, Vlissides) in 1994 — **23 patterns**.

### Why use them? (Part I, slide 7)

Proven solution · Reusable · Expressive · Prevents later refactoring · Smaller codebase.

### The four key components of a pattern (Part I, slide 10)

Be ready to describe *your* pattern using these four headings — every module
guide in this folder is laid out this way:

| Component | Question it answers |
|---|---|
| **Name** | What do we call it? |
| **Problem** | When should it be applied — what is it trying to solve? |
| **Solution** | Which classes/interfaces, their relationships and responsibilities? |
| **Consequences** | What are the benefits and costs of applying it? |

### Classification (Part I, slides 11–12)

| Category | Deals with | Patterns in our system |
|---|---|---|
| **Creational** | Object creation | Factory (User, Payment), Singleton (Payment) |
| **Structural** | Relationships / composing objects into larger structures | Decorator (Cart) |
| **Behavioral** | Communication between objects, assigning responsibilities | Observer (Support), Strategy (Payment, Catalog, Feedback) |

---

## 3. How to present your pattern (suggested 2–3 minutes)

1. **Name + category** — "My module uses the *Strategy* pattern, a *behavioral* pattern."
2. **Problem** — what would the code look like *without* it (usually a long `if/else` or `switch`, or many subclasses).
3. **Show the classes** — open the package in the IDE; point at the interface, then the concrete classes, then the class that uses them. Use the "role mapping" table in your guide.
4. **Demo it running** — each guide has a "How to demo" section with exact clicks.
5. **Consequences** — one advantage and one disadvantage *from the slides*, applied to our system.
6. **Extension** — "To add a new X, we only write one new class" (Open/Closed Principle).

---

## 4. When the examiner says "show us how it works" — and asks nothing else

Examiners may not ask questions at all. They may just say *"demonstrate your
design pattern"* or *"explain it to us"* and watch. Then **you** have to cover
everything that would have come out in a Q&A. Section 8 of every module guide
has a step-by-step **Do / Say** script for this. These rules apply to all of them:

**Before the session**
- **Prepare the data.** Each guide's "How to demo" section says what must already
  exist (e.g. a cart over Rs. 50,000, a product with reviews, an inquiry linked to
  an order). Set it up the day before — don't create it live.
- **Open the files in advance** in IDE tabs, in the order your script uses them:
  interface → concrete classes → the class that uses them → where it's triggered.
- **Have the test class ready to run** (right-click → *Run*). If the live demo
  fails, a green test run is your backup evidence that the pattern works.
- **Rehearse aloud once, with a timer.** Each script is 3–4 minutes.

**During the demonstration — cover these six points, in this order**

| # | Point | Example sentence |
|---|---|---|
| 1 | **Name + category** | "I used the Decorator pattern, a structural pattern." |
| 2 | **Lecture definition** in one sentence | "It attaches new behaviour to an object at runtime by wrapping it." |
| 3 | **The problem in our system** | "Without it, every combination of charges would need its own class." |
| 4 | **Each role → our class** (show the code) | "This interface is the Component, this class is the Concrete Component…" |
| 5 | **It running** (browser, console or database) + the test | "Here's the discount line appearing for a large cart." |
| 6 | **One benefit + one cost** from the slides | "Easy to add a new charge; the cost is more small classes." |

Covering all six without being asked shows *understanding*, which is what the
5 marks are for. Finish with the extension sentence: **"To add a new ___, we
would only write one new class."**

**Tips**
- **Say the lecture's words for each role** (Context, Concrete Strategy,
  Concrete Observer, Component…) while pointing at the class. That is the
  clearest way to show you know the pattern, not just the code.
- **Show, then tell.** Click in the browser first ("watch the delivery line"),
  then open the class responsible.
- **Don't read code line by line.** Point at the class name, the interface it
  implements, and the one line that matters (e.g. `strategy.pay(order)`).
- **If something breaks live**, say what should have happened, then run the
  unit test. Stay calm — the explanation is still marked.
- **Mention a design decision you made** (e.g. the Observer's try/catch per
  observer, the Singleton's `synchronized`). It shows the code is yours and understood.

---

## 5. Running the system for the demo

- Use **JDK 17** (the project's `java.version`). JDK 24+ breaks Mockito in the tests.
- Start MySQL, then from the project root: `mvn spring-boot:run` → <http://localhost:8081>
- Run the tests: `mvn test` (31 tests, including one test class per pattern).

Seeded logins (created by `DataSeedConfig`):

| Role | Email | Password |
|---|---|---|
| Platform employee | `admin@mall.local` | `admin123` |
| Customer | `cus2@mail.com` … `cus9@mail.com` | the digit 6 times, e.g. `222222` |
| Merchant | `merc1@mail.com` … (one per sample shop) | the digit 6 times, e.g. `111111` |

---

## 6. Questions any member might be asked

**Q: Why use design patterns at all — isn't it just more classes?**
A: They give proven, well-understood structures. The cost is more classes (every
pattern's "disadvantages" slide says so), but in return the code is easier to
extend without editing working code (Open/Closed Principle), and the team shares
a vocabulary. We only used patterns where there were genuinely several
interchangeable behaviours or object types.

**Q: What is the difference between Strategy and Factory?**
A: *Factory* (creational) decides **which object to create**. *Strategy*
(behavioral) decides **which algorithm to run**. In the payment module they work
together: the factory *creates* the right payment strategy, then the service
*runs* it.

**Q: What is the difference between Strategy and Decorator?**
A: Strategy **replaces** one algorithm with another (exactly one is used).
Decorator **wraps** an object to **add** behaviour on top — several can be
stacked (subtotal → discount → delivery).

**Q: What is the difference between Observer and Strategy?**
A: Observer is about **notification** — one subject, *many* listeners, all told
when something changes. Strategy is about **choosing one** algorithm.

**Q: Spring already makes every `@Service` a singleton. Is that the Singleton pattern?**
A: Spring beans are "singleton-scoped" — the container creates one instance —
but the class itself doesn't enforce it. The lecture's Singleton enforces it
inside the class (private constructor + static `getInstance()`), which is what
`TransactionReferenceGenerator` does (see guide 02).

**Q: Which SOLID principles do the patterns support?**
A: **Open/Closed** (add a new class instead of editing existing ones — Strategy,
Factory, Decorator, Observer) and **Single Responsibility** (each algorithm,
add-on or notifier lives in its own class). The Strategy slides (Part II,
slide 23) name both explicitly.
