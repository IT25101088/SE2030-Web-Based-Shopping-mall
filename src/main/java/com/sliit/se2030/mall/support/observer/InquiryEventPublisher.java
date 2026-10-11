package com.sliit.se2030.mall.support.observer;

import com.sliit.se2030.mall.support.entity.Inquiry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Observer pattern -- the Concrete Subject. InquiryService calls
 * notifyObservers() after each change; this class doesn't know or care what
 * the observers do with it. Adding a new reaction (e.g. an email sender) means
 * writing one new InquiryObserver class -- InquiryService doesn't change.
 *
 * Spring passes in every InquiryObserver bean at startup, so they're
 * registered automatically. addObserver/removeObserver still work at runtime.
 */
@Component
public class InquiryEventPublisher implements InquirySubject {

    private static final Logger log = LoggerFactory.getLogger(InquiryEventPublisher.class);

    // Copy-on-write: safe if an observer is added or removed while notifying.
    private final List<InquiryObserver> observers = new CopyOnWriteArrayList<>();

    public InquiryEventPublisher(List<InquiryObserver> observers) {
        this.observers.addAll(observers);
    }

    @Override
    public void addObserver(InquiryObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(InquiryObserver observer) {
        observers.remove(observer);
    }

    // A failing observer must never undo the inquiry change itself, so each
    // one's error is logged and swallowed instead of rolling back the request.
    @Override
    public void notifyObservers(InquiryEvent event, Inquiry inquiry) {
        for (InquiryObserver observer : observers) {
            try {
                observer.update(event, inquiry);
            } catch (RuntimeException e) {
                log.warn("Inquiry observer {} failed on {}", observer.getClass().getSimpleName(), event, e);
            }
        }
    }
}
