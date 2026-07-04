package com.escaperoom.pricing;

import com.escaperoom.model.Room;
import java.time.LocalDateTime;

/**
 * STRATEGY PATTERN.
 *
 * Each implementation encapsulates one way of turning
 * (room, group size, time slot) into a price. VenueManager holds
 * a reference to whichever strategy is currently active and can
 * swap it at runtime via setPricingStrategy() - no if/else on
 * "pricing mode" anywhere else in the codebase.
 */
public interface PricingStrategy {
    double calculatePrice(Room room, int groupSize, LocalDateTime timeSlot);

    String getName();
}
