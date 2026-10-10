package com.sliit.se2030.mall.support.observer;

import com.sliit.se2030.mall.support.entity.Inquiry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer pattern -- a Concrete Observer for shops. A shop only needs to know
 * when the mall team forwards an inquiry to it.
 */
@Component
public class MerchantNotifier implements InquiryObserver {

    private static final Logger log = LoggerFactory.getLogger(MerchantNotifier.class);

    @Override
    public void update(InquiryEvent event, Inquiry inquiry) {
        if (event == InquiryEvent.FORWARDED_TO_SHOP && inquiry.getMerchant() != null) {
            log.info("[Notify shop {}] Inquiry #{} \"{}\" was forwarded to your shop", inquiry.getMerchant().getShopName(),
                    inquiry.getId(), inquiry.getSubject());
        }
    }
}
