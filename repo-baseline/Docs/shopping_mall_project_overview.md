# Web-Based Multi-Merchant Shopping Mall System — Project Overview

**Module:** IT2030 – Software Engineering (SLIIT)
**Tech stack:** Java, Spring Boot, JSP (presentation layer), Microsoft SQL Server (database)
**Architecture:** Three-tier — Presentation (JSP) → Business Logic (Spring Boot services) → Data Access (MSSQL)

## 1. Project Summary

This project is a web-based multi-merchant shopping mall system that allows multiple independent merchants to register and sell products through a single, unified platform, while customers browse, purchase, review, and get support across all merchants in one place. Unlike single-seller e-commerce sites, this system's core identity is its multi-merchant nature.

Note: The instructor explicitly stated that **user management and payment should NOT be standalone functional requirements**. They are folded into other functions (see below).

## 2. Main Objective

To design and develop a web-based multi-merchant shopping mall system that enables merchants to sell products and customers to browse, purchase, and interact with those products within a single, unified platform.

## 3. Six Core Functional Requirements (current, updated names)

1. **User & Stakeholder Management** *(renamed from "Merchant Management" — broadened scope)*
2. **Product Catalog Management**
3. **Shopping Cart Handling**
4. **Order and Payment Handling**
5. **Product Feedback & Reputation Analytics** *(renamed from "Product Review and Rating System" — elevated scope)*
6. **Customer Support and Inquiry System**

### 3.1 User & Stakeholder Management
Centralized identity and role administration engine for the platform — manages registration, verification, and access control for every actor type: customers, merchants, and platform employees. Not just merchant-only anymore; frames authentication/roles as core infrastructure rather than a side feature.

Capabilities:
- Customer registration and account management
- Merchant registration, shop profile setup, and product catalog ownership
- Platform employee verification/approval/suspension of merchant accounts
- Role-based access control (each actor limited to permitted actions)
- Merchant-side: add/edit/remove products, manage orders, track inventory

### 3.2 Product Catalog Management
- Categorize products
- Search by name/keyword
- Filter by category, price range, merchant
- Detailed product pages (description, price, images, stock, merchant info)
- Unified catalog across multiple merchants

### 3.3 Shopping Cart Handling
- Add/update/remove cart items
- Running cart total
- Cart persistence across session

### 3.4 Order and Payment Handling
- Checkout process (cart → order)
- Payment (simulated/mocked — no real gateway integration)
- Order confirmation and summary
- Order status tracking (pending/shipped/delivered)
- Order history for customers
- Merchant-side order management (view/accept/update status)

### 3.5 Product Feedback & Reputation Analytics
Elevated from basic CRUD reviews into an analytics-driven trust system:
- Verified-purchase-only reviews (star rating + written review)
- Rating distribution breakdown per product (not just average)
- Aggregated merchant reputation score (across all their products)
- Merchant feedback dashboard (rating trends, top/lowest-rated products, recent reviews)
- Merchant ability to publicly respond to reviews
- Automatic flagging of products for platform employee attention when ratings fall below a defined threshold

### 3.6 Customer Support and Inquiry System
- Submit inquiry/complaint tied to an order or product
- Track inquiry status (open/in progress/resolved)
- Platform employee/merchant response mechanism
- Optional FAQ section

## 4. Target Users / Stakeholders (3 roles — no "delivery partners")
- **Customer** — browses, purchases, reviews products, raises inquiries
- **Merchant** — manages store, products, orders, views feedback analytics
- **Platform Employee / Admin** — verifies merchants, resolves escalated inquiries, monitors flagged products

## 5. Scope and Limitations

**In scope:** the six core functions above, plus supporting/minor functions (registration/login, password reset) that exist to support the core functions rather than as standalone deliverables.

**Out of scope / limitations:**
- No real payment gateway integration (payment is simulated/mocked)
- No delivery/logistics tracking or courier integration
- Web-only, no native mobile app
- Single language/currency only
- No AI/ML-based recommendations, personalization, or dynamic pricing
- Feedback analytics are rule-based (fixed threshold flagging), not ML/sentiment-based

## 6. Non-Functional Requirements
- **Performance** — responsive catalog/search even as data grows; optimized MSSQL indexing
- **Security** — password hashing, secure session management, input validation/sanitization (SQL injection prevention), scoped merchant access to their own data only
- **Usability** — simple, consistent JSP-based UI for both technical and non-technical users
- **Reliability** — no data loss during checkout/payment; accurate order records
- **Scalability** — layered Spring Boot architecture supports growth without redesign
- **Maintainability** — clear separation of concerns (presentation/business/data layers)

## 7. System Limitations / Constraints

**Technical:**
- Web-based only, standard desktop browsers (Chrome, Firefox, Edge)
- Requires Java runtime + compatible app server; MSSQL required for the database layer
- Developed/tested in a local/academic environment, not production-grade cloud deployment

**Functional:**
- Payment is simulated, not real
- No real-time delivery tracking (manual status updates by merchants)
- Single language/currency
- Rule-based (not ML-based) feedback analytics
- No AI-driven recommendations, dynamic pricing, or advanced search

**Assumptions:**
- Merchants provide accurate registration info; verification is basic, not legal/business validation
- Customers have basic digital literacy and browser/internet access
- Moderate concurrent user load (academic project scale, not enterprise traffic)
- Sample/dummy data used during development/testing
- Single or small number of hardcoded platform employee/admin accounts for demo purposes

## 8. Development Methodology
Agile approach with iterative sprints. Product backlog derived from the six major functions. Tasks prioritized by complexity and dependency. Each sprint ends with a review to assess progress and plan next steps. Progress documented in an Agile Sprint Summary (Week 10 design document, per proposal timeline).

## 9. Conclusion (Proposal Report)
The web-based shopping mall brings together independent merchants and customers on a single, accessible platform, replacing fragmented, manual processes with a structured system for browsing, purchasing, and after-sales support. By focusing on six core functions — user and stakeholder management, product catalogue management, shopping cart handling, order and payment handling, product feedback and reputation analytics, and customer support — the system addresses the essential needs of its three stakeholder groups: customers, merchants, and platform employees. The group has divided responsibility for each core function among its six members, with a clear week-by-week plan from Week 3 through Week 14 covering design, development, integration, and testing.

---

# Lab 02 — Agile/Scrum Deliverable (Separate Report)

## Group Members & Roles
| Member | Scrum Role | Function Owned |
|---|---|---|
| Bandara N.V. | Product Owner | Product Catalog Management |
| Ariyasinghe P.A.D.A.J. | Scrum Master | Customer Support and Inquiry |
| Vaas W.P.S.D. | Developer | Shopping Cart Handling |
| Maliduwa M.G.B.B. | Developer | Order and Payment Handling |
| Weerawickrama K.K. | Developer | Product Review and Rating |
| Neluvinda T.M.D.N. | Developer | Merchant Management |

## 6 Personas (mapped to functions)
1. **Browsing shopper** (Customer) → Product Catalog Management
2. **Support agent** (Platform Employee) → Customer Support and Inquiry
3. **Multi-item shopper** (Customer) → Shopping Cart Handling
4. **Frequent buyer** (Customer) → Order and Payment Handling
5. **Engaged reviewer** (Customer) → Product Review and Rating
6. **New merchant** (Merchant) → Merchant Management

## Product Backlog (24 user stories, PBI-01 to PBI-24)
Prioritized High/Medium/Low. High-priority items: merchant registration/profile, product CRUD, search, product details, add to cart, payment, order confirmation, merchant order management. Medium: filters, cart quantity/removal/total, order tracking, review submission/display/verification, inquiry submission/handling. Low: order history, merchant feedback view, customer inquiry status view.

## Sprint Plan (4 sprints, Week 3–12)
- **Sprint 1 (Wk 3–5):** Merchant registration, product search, add to cart, secure payment, review submission, inquiry submission
- **Sprint 2 (Wk 6–8):** Product CRUD, product details, cart quantity update, order confirmation, average rating/reviews display, view submitted inquiries
- **Sprint 3 (Wk 9–10):** Merchant order management, filters, cart item removal, order tracking, verified-purchase review restriction, inquiry status updates
- **Sprint 4 (Wk 11–12):** Inventory tracking, multi-merchant catalog browsing, cart running total, order history, merchant feedback dashboard, customer inquiry status view

Each sprint's user stories are broken into 4–5 sub-tasks with hour estimates, assigned to the developer who owns that function.

## UML Use Case Notes
- 3 actors: Customer, Merchant, Platform Employee
- Include relationships (mandatory): Checkout includes Make Payment; Register/Login includes Validate Credentials; Submit Review includes Calculate Average Rating; Approve/Suspend Merchant includes Verify Merchant Details
- Extend relationships (conditional): Submit Review extends View Order History (condition: verified purchase); Flag Low-Rated Product extends Calculate Average Rating (condition: below threshold); Respond to Review extends View Feedback Dashboard (optional)
- Authentication is NOT modeled as an include on every protected action (avoids diagram clutter) — treated as an implicit precondition instead, only explicitly modeled once under Register/Login.
