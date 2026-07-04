package com.escaperoom.observer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A concrete Observer that simulates "notifying staff" by printing
 * a timestamped message to the console. In a real system this could
 * be swapped for an email/SMS/push-notification observer without
 * touching VenueEventPublisher or VenueManager at all.
 */
public class StaffNotifier implements VenueObserver {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void onEvent(String eventMessage) {
        System.out.println("  [NOTIFY " + LocalTime.now().format(TIME_FORMAT) + "] " + eventMessage);
    }
}
