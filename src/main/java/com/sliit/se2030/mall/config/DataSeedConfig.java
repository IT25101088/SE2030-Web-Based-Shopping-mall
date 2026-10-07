package com.sliit.se2030.mall.config;

import com.sliit.se2030.mall.catalog.entity.Category;
import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.repository.CategoryRepository;
import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import com.sliit.se2030.mall.config.SampleCatalogData.SampleProduct;
import com.sliit.se2030.mall.config.SampleCatalogData.SampleShop;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.entity.User;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import com.sliit.se2030.mall.user.repository.PlatformEmployeeRepository;
import com.sliit.se2030.mall.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

/**
 * Seeds one hardcoded platform employee account so there's always a way to
 * log in and approve merchants, even before the registration UI exists.
 * Academic-project scale (see project docs): "single/small number of
 * hardcoded platform employee accounts for demo purposes" is explicitly
 * in scope, not a shortcut we're inventing.
 *
 * Also seeds sample customers, merchants and their shops' products for demos. Every account is
 * skipped if its email already exists, so restarting the app is safe.
 */
@Configuration
public class DataSeedConfig {

    private static final String SEED_EMAIL = "admin@mall.local";
    private static final String SEED_PASSWORD = "admin123";

    @Bean
    @Order(1)
    CommandLineRunner seedPlatformEmployee(UserRepository userRepository,
                                            PlatformEmployeeRepository platformEmployeeRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.existsByEmail(SEED_EMAIL)) {
                return;
            }
            PlatformEmployee employee = new PlatformEmployee(
                    SEED_EMAIL, passwordEncoder.encode(SEED_PASSWORD), "Platform Admin");
            employee.setEmployeeCode("EMP-001");
            platformEmployeeRepository.save(employee);
            System.out.println("Seeded platform employee login: " + SEED_EMAIL + " / " + SEED_PASSWORD);
        };
    }

    /**
     * Sample accounts following a simple numbered pattern, e.g. for n = 3:
     *   customer: cus3@mail.com / name "cus3"  / phone "3333" / password "333333"
     *   merchant: merc3@mail.com / name "merc3" / password "333333"
     * Each merchant gets a uniquely named, stocked shop from SampleCatalogData.
     * Runs after seedPlatformEmployee (@Order 2) so the admin exists and can be
     * recorded as the employee who approved the seeded merchants.
     */
    @Bean
    @Order(2)
    CommandLineRunner seedSampleUsers(UserRepository userRepository,
                                       CustomerRepository customerRepository,
                                       MerchantRepository merchantRepository,
                                       CategoryRepository categoryRepository,
                                       ProductRepository productRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            for (int n = 2; n <= 9; n++) {
                String email = "cus" + n + "@mail.com";
                if (userRepository.existsByEmail(email)) {
                    continue;
                }
                Customer customer = new Customer(email, passwordEncoder.encode(repeat(n, 6)), "cus" + n);
                customer.setPhone(repeat(n, 4));
                customerRepository.save(customer);
            }

            Long adminId = userRepository.findByEmail(SEED_EMAIL).map(User::getId).orElse(null);
            for (SampleShop shop : SampleCatalogData.SHOPS) {
                int n = shop.number();
                String email = "merc" + n + "@mail.com";
                Merchant merchant = merchantRepository.findByEmail(email).orElse(null);
                if (merchant == null) {
                    merchant = new Merchant(
                            email, passwordEncoder.encode(repeat(n, 6)), "merc" + n, shop.shopName());
                    // Seeded shops start approved so they can list products straight away.
                    merchant.setVerificationStatus(VerificationStatus.APPROVED);
                    merchant.setVerifiedAt(Instant.now());
                    merchant.setVerifiedByEmployeeId(adminId);
                    merchant.setLogoUrl(shop.logoUrl());
                    merchant = merchantRepository.save(merchant);
                } else {
                    if (("shop" + n).equals(merchant.getShopName())) {
                        // Databases seeded by an earlier version still have the "shopN"
                        // placeholder name; a name the merchant chose is left alone.
                        merchant.setShopName(shop.shopName());
                    }
                    if (merchant.getLogoUrl() == null) {
                        // Seeded before logos existed. null means "never set"; a merchant
                        // who cleared their logo has "" saved, so their choice is kept.
                        merchant.setLogoUrl(shop.logoUrl());
                    }
                    merchant = merchantRepository.save(merchant);
                }
                seedProducts(merchant, shop, productRepository, categoryRepository);
            }
        };
    }

    // Only stocks a shop that has no products yet, so restarts don't add duplicates.
    private static void seedProducts(Merchant merchant, SampleShop shop,
                                     ProductRepository productRepository,
                                     CategoryRepository categoryRepository) {
        if (!productRepository.findByMerchant_Id(merchant.getId()).isEmpty()) {
            return;
        }
        Category category = categoryRepository.findByName(shop.categoryName())
                .orElseGet(() -> categoryRepository.save(new Category(shop.categoryName())));
        for (SampleProduct item : shop.products()) {
            Product product = new Product(item.name(), item.price(), item.stock(), merchant);
            product.setDescription(item.description());
            product.setImageUrl(item.imageUrl());
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    // repeat(3, 4) -> "3333"
    private static String repeat(int digit, int times) {
        return String.valueOf(digit).repeat(times);
    }
}
