</main>

<footer class="site-footer">
    <div class="container">
        <div class="footer-grid">
            <div class="footer-about">
                <a class="brand" href="${pageContext.request.contextPath}/">
                    <span class="brand__mark" aria-hidden="true">S</span>
                    <span class="brand__name">Serendib Central</span>
                </a>
                <p>Sri Lankan shops in one online mall. Buy from several sellers in a single checkout.</p>
            </div>
            <div>
                <h2>Shop</h2>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/catalog">All products</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart">Your cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders">Track an order</a></li>
                </ul>
            </div>
            <div>
                <h2>Sell</h2>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/register/merchant">Open a shop</a></li>
                    <li><a href="${pageContext.request.contextPath}/merchant/dashboard">Merchant login</a></li>
                </ul>
            </div>
            <div>
                <h2>Help</h2>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/faq">FAQ</a></li>
                    <li><a href="${pageContext.request.contextPath}/inquiries/new">Ask a question</a></li>
                </ul>
            </div>
        </div>
        <p class="footer-base">SE2030 Group 07 project, built for learning. Payments are simulated and no real money changes hands.</p>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/serendib.js"></script>
</body>
</html>
