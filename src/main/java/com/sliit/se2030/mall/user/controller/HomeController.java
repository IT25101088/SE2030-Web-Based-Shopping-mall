package com.sliit.se2030.mall.user.controller;

import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.service.CategoryService;
import com.sliit.se2030.mall.catalog.service.ProductService;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * The public landing page ("/") plus one home page per role, matching where
 * RoleBasedAuthenticationSuccessHandler sends people after login.
 */
@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final DashboardService dashboardService;

    public HomeController(ProductService productService, CategoryService categoryService,
                          DashboardService dashboardService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.dashboardService = dashboardService;
    }

    // Landing page. Reuses the catalog's browse() with no filters (= every
    // active product); the JSP only shows the first few of them.
    @GetMapping("/")
    public String root(Model model) {
        List<Product> products = productService.browse(null, null, null, null);

        // One entry per shop that currently has something on sale, for the
        // scrolling "shops in the mall" strip. Whole Merchant objects (not just
        // names) so the strip can show each shop's logo. distinct() is safe here:
        // all products come from one query, so the same shop is the same object.
        List<Merchant> shops = products.stream()
                .map(Product::getMerchant)
                .distinct()
                .toList();

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("shops", shops);
        return "common/home";
    }

    // The three role home pages each get a "dashboard" object holding the
    // live numbers for that person (see DashboardService).
    @GetMapping("/customer/home")
    public String customerHome(Model model) {
        model.addAttribute("dashboard", dashboardService.forCustomer());
        return "user/customer-home";
    }

    @GetMapping("/merchant/dashboard")
    public String merchantDashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.forMerchant());
        model.addAttribute("lowStockLimit", DashboardService.LOW_STOCK_LIMIT);
        return "user/merchant-dashboard";
    }

    @GetMapping("/employee/dashboard")
    public String employeeDashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.forEmployee());
        return "user/employee-dashboard";
    }
}
