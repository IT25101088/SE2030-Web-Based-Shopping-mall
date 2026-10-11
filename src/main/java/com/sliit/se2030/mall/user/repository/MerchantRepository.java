package com.sliit.se2030.mall.user.repository;

import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    // Powers the platform employee's "pending approvals" list.
    List<Merchant> findByVerificationStatus(VerificationStatus status);

    // Powers the employee's "Shops" page, which lists every shop whatever its status.
    List<Merchant> findAllByOrderByShopNameAsc();

    long countByVerificationStatus(VerificationStatus status);

    // "Pending shops that registered after this time" -- the unseen-shops badge.
    long countByVerificationStatusAndCreatedAtAfter(VerificationStatus status, Instant after);

    Optional<Merchant> findByEmail(String email);
}
