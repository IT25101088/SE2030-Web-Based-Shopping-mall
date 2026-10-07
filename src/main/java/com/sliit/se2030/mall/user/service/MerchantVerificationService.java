package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import com.sliit.se2030.mall.user.repository.PlatformEmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class MerchantVerificationService {

    private final MerchantRepository merchantRepository;
    private final PlatformEmployeeRepository platformEmployeeRepository;

    public MerchantVerificationService(MerchantRepository merchantRepository,
                                       PlatformEmployeeRepository platformEmployeeRepository) {
        this.merchantRepository = merchantRepository;
        this.platformEmployeeRepository = platformEmployeeRepository;
    }

    public List<Merchant> listAll() {
        return merchantRepository.findAllByOrderByShopNameAsc();
    }

    public List<Merchant> listPending() {
        return merchantRepository.findByVerificationStatus(VerificationStatus.PENDING);
    }

    // How many pending shops this employee hasn't seen yet: the ones that
    // registered after they last opened the pending list (or all of them if
    // they've never opened it).
    public long countUnseenPending(Long employeeId) {
        Instant seenAt = findEmployeeOrThrow(employeeId).getPendingShopsSeenAt();
        if (seenAt == null) {
            return merchantRepository.countByVerificationStatus(VerificationStatus.PENDING);
        }
        return merchantRepository.countByVerificationStatusAndCreatedAtAfter(VerificationStatus.PENDING, seenAt);
    }

    // Called when the employee opens the pending list. Returns the PREVIOUS
    // time so the page can still mark which shops are new on this visit.
    @Transactional
    public Instant markPendingSeen(Long employeeId) {
        PlatformEmployee employee = findEmployeeOrThrow(employeeId);
        Instant previous = employee.getPendingShopsSeenAt();
        employee.setPendingShopsSeenAt(Instant.now());
        return previous;
    }

    @Transactional
    public void approveMerchant(Long merchantId, Long employeeId) {
        Merchant merchant = findMerchantOrThrow(merchantId);
        merchant.setVerificationStatus(VerificationStatus.APPROVED);
        merchant.setVerifiedAt(Instant.now());
        merchant.setVerifiedByEmployeeId(employeeId);
        merchant.setEnabled(true);
    }

    @Transactional
    public void suspendMerchant(Long merchantId, Long employeeId) {
        Merchant merchant = findMerchantOrThrow(merchantId);
        merchant.setVerificationStatus(VerificationStatus.SUSPENDED);
        merchant.setVerifiedByEmployeeId(employeeId);
        // enabled=false blocks login outright at the Spring Security level --
        // see AppUserPrincipal.isEnabled(). A suspended merchant can't even
        // reach the login page's "wrong credentials" state, they're just refused.
        // Their products also drop out of the catalog -- see Product.isOnSale().
        merchant.setEnabled(false);
    }

    // Undoes a suspension: the shop is APPROVED again and the owner can log in.
    @Transactional
    public void reinstateMerchant(Long merchantId, Long employeeId) {
        Merchant merchant = findMerchantOrThrow(merchantId);
        merchant.setVerificationStatus(VerificationStatus.APPROVED);
        merchant.setVerifiedByEmployeeId(employeeId);
        merchant.setEnabled(true);
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

    private PlatformEmployee findEmployeeOrThrow(Long employeeId) {
        return platformEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
    }
}
