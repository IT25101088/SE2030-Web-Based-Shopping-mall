package com.sliit.se2030.mall.support.observer;

import com.sliit.se2030.mall.support.entity.Inquiry;

/**
 * Observer pattern -- the Subject interface: keeps a list of observers and
 * tells all of them when something happens to an inquiry.
 */
public interface InquirySubject {

    void addObserver(InquiryObserver observer);

    void removeObserver(InquiryObserver observer);

    void notifyObservers(InquiryEvent event, Inquiry inquiry);
}
