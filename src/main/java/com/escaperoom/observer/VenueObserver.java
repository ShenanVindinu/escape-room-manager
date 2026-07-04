package com.escaperoom.observer;

/**
 * OBSERVER PATTERN.
 *
 * Anything that wants to react to venue events (a room freeing up,
 * a room filling up, a session running long) implements this and
 * registers with a VenueEventPublisher. The publisher doesn't know
 * or care what the observers do with the event.
 */
public interface VenueObserver {
    void onEvent(String eventMessage);
}
