package com.sliit.se2030.mall.support.observer;

import com.sliit.se2030.mall.support.entity.Inquiry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer pattern -- a Concrete Observer for the mall team. They need to know
 * about new inquiries and about shops handing an inquiry back for review.
 */
@Component
public class EmployeeNotifier implements InquiryObserver {

    private static final Logger log = LoggerFactory.getLogger(EmployeeNotifier.class);

    @Override
    public void update(InquiryEvent event, Inquiry inquiry) {
        String message = switch (event) {
            case SUBMITTED -> "a new help request is waiting";
            case SHOP_REPLIED -> "a shop answered; ready for your review";
            default -> null;
        };
        if (message != null) {
            log.info("[Notify mall team] Inquiry #{} \"{}\": {}", inquiry.getId(), inquiry.getSubject(), message);
        }
    }
}
