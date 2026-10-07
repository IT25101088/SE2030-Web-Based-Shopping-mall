package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class MerchantVerificationService {

    private final MerchantRepository merchantRepository;

    public MerchantVerificationService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public List<Merchant> listPending() {
        return merchantRepository.findByVerificationStatus(VerificationStatus.PENDING);
    }

    @Transactional
    public void approveMerchant(Long merchantId, Long employeeId) {
        Merchant merchant = findMerchantOrThrow(merchantId);
        merchant.setVerificationStatus(VerificationStatus.APPROVED);
        merchant.setVerifiedAt(Instant.now());
        merchant.setVerifiedByEmployeeId(employeeId);
    }

    @Transactional
    public void suspendMerchant(Long merchantId, Long employeeId) {
        Merchant merchant = findMerchantOrThrow(merchantId);
        merchant.setVerificationStatus(VerificationStatus.SUSPENDED);
        merchant.setVerifiedByEmployeeId(employeeId);
        // enabled=false blocks login outright at the Spring Security level --
        // see AppUserPrincipal.isEnabled(). A suspended merchant can't even
        // reach the login page's "wrong credentials" state, they're just refused.
        merchant.setEnabled(false);
    }

    @Transactional
    public void rejectMerchant(Long merchantId, Long employeeId) {
        Merchant merchant = findMerchantOrThrow(merchantId);
        merchant.setVerificationStatus(VerificationStatus.REJECTED);
        merchant.setVerifiedByEmployeeId(employeeId);
    }

    private Merchant findMerchantOrThrow(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found: " + merchantId));
    }
}
