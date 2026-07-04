package com.escaperoom.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * The "Subject" half of the Observer pattern. Keeps a list of observers
 * and notifies all of them whenever something noteworthy happens at
 * the venue (room booked, freed, cleaning finished, etc).
 */
public class VenueEventPublisher {
    private final List<VenueObserver> observers = new ArrayList<>();

    public void subscribe(VenueObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(VenueObserver observer) {
        observers.remove(observer);
    }

    public void publish(String eventMessage) {
        for (VenueObserver observer : observers) {
            observer.onEvent(eventMessage);
        }
    }
}
