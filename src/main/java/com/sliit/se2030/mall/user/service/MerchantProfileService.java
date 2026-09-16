package com.sliit.se2030.mall.user.service;

import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.user.dto.ShopProfileForm;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantProfileService {

    private final MerchantRepository merchantRepository;
    private final CurrentUserProvider currentUserProvider;

    public MerchantProfileService(MerchantRepository merchantRepository, CurrentUserProvider currentUserProvider) {
        this.merchantRepository = merchantRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Merchant getOwnProfile() {
        return findOwnMerchantOrThrow();
    }

    @Transactional
    public void updateShopProfile(ShopProfileForm form) {
        // TODO: implement updateShopProfile -- see your NOTES.md,
        // "MerchantProfileService.updateShopProfile()". Remember: "which merchant"
        // must come from findOwnMerchantOrThrow()/CurrentUserProvider, never a
        // client-supplied id.
        throw new UnsupportedOperationException("TODO: implement updateShopProfile()");
    }

    // No id parameter anywhere in this class -- "which merchant" always
    // comes from the authenticated session, never from client-supplied input.
    private Merchant findOwnMerchantOrThrow() {
        Long id = currentUserProvider.getCurrentUserId();
        return merchantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found: " + id));
    }
}
