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
        // TODO: implement approveMerchant -- see your NOTES.md,
        // "MerchantVerificationService.approveMerchant()" (set status APPROVED,
        // verifiedAt, verifiedByEmployeeId).
        throw new UnsupportedOperationException("TODO: implement approveMerchant()");
    }

    @Transactional
    public void suspendMerchant(Long merchantId, Long employeeId) {
        // TODO: implement suspendMerchant -- see your NOTES.md,
        // "MerchantVerificationService.suspendMerchant()". Remember: setEnabled(false)
        // is what actually blocks login, not just the verificationStatus field.
        throw new UnsupportedOperationException("TODO: implement suspendMerchant()");
    }

    @Transactional
    public void rejectMerchant(Long merchantId, Long employeeId) {
        // TODO: implement rejectMerchant -- see your NOTES.md,
        // "MerchantVerificationService.rejectMerchant()".
        throw new UnsupportedOperationException("TODO: implement rejectMerchant()");
    }

    private Merchant findMerchantOrThrow(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found: " + merchantId));
    }
}
