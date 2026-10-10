package com.sliit.se2030.mall.support.observer;

import com.sliit.se2030.mall.support.entity.Inquiry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Observer pattern -- a Concrete Observer for the customer who raised the
 * inquiry. Only reacts to what the customer can actually see: shops' notes
 * are internal, so SHOP_REPLIED and forwarding are ignored here.
 *
 * For now the "notification" is a log line; swapping it for an email or an
 * in-app message only changes this class.
 */
@Component
public class CustomerNotifier implements InquiryObserver {

    private static final Logger log = LoggerFactory.getLogger(CustomerNotifier.class);

    @Override
    public void update(InquiryEvent event, Inquiry inquiry) {
        String message = switch (event) {
            case SUBMITTED -> "we received your help request";
            case EMPLOYEE_REPLIED -> "the mall team replied to your help request";
            case RESOLVED -> "your help request has been resolved";
            default -> null;
        };
        if (message != null) {
            log.info("[Notify customer {}] Inquiry #{} \"{}\": {}", inquiry.getCustomer().getEmail(),
                    inquiry.getId(), inquiry.getSubject(), message);
        }
    }
}
