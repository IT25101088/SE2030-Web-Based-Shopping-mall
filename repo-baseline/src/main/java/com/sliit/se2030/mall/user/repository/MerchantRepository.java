package com.sliit.se2030.mall.user.repository;

import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    // Powers the platform employee's "pending approvals" list.
    List<Merchant> findByVerificationStatus(VerificationStatus status);

    Optional<Merchant> findByEmail(String email);
}
