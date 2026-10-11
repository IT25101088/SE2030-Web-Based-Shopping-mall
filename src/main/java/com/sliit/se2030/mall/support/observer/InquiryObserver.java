package com.sliit.se2030.mall.support.observer;

import com.sliit.se2030.mall.support.entity.Inquiry;

/**
 * Observer pattern -- the Observer interface. Anything that wants to react
 * when an inquiry changes implements update(). Spring finds every @Component
 * that implements this and registers it with InquiryEventPublisher.
 */
public interface InquiryObserver {

    void update(InquiryEvent event, Inquiry inquiry);
}
